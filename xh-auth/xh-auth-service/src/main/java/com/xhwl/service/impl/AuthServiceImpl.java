package com.xhwl.service.impl;

import com.xhwl.commons.utils.JsonUtils;
import com.xhwl.service.AuthService;
import com.xhwl.utils.AuthToken;
import com.xhwl.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.security.Key;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Value("${auth.ttl}")
    private long ttl;

    /**
     * 刷新令牌
     *
     * @param grant_type
     * @param refresh_token
     * @param clientId
     * @param clientSecret
     * @param original_jti
     * @return
     */
    @Override
    public AuthToken refreshToken(String grant_type, String refresh_token, String original_jti, String clientId, String clientSecret) {
        //1.申请令牌
        ServiceInstance serviceInstance = loadBalancerClient.choose("auth-service");
        URI uri = serviceInstance.getUri();
        String url = uri + "/oauth/token";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grant_type);
        body.add("refresh_token", refresh_token);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.getHttpBasic(clientId, clientSecret));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = responseEntity.getBody();
        if (map == null || map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //申请令牌失败
            throw new RuntimeException("刷新令牌失败");
        }
        //2.封装结果数据
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken((String) map.get("access_token"));
        authToken.setRefreshToken((String) map.get("refresh_token"));
        authToken.setJti((String) map.get("jti"));
        //删除原来的旧的authToken对象
        Boolean aBoolean = stringRedisTemplate.delete(original_jti);
        if (aBoolean) {
            log.println("删除旧token成功");
        } else {
            log.println("删除旧token失败");
        }

        //3.将jti作为redis中的key,将jwt作为redis中的value进行数据的存放
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(JsonUtils.toString(authToken), ttl, TimeUnit.SECONDS);
        return authToken;
    }

    /**
     * 申请令牌
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Override
    public AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

        //1.申请令牌
        ServiceInstance serviceInstance = loadBalancerClient.choose("auth-service");
        URI uri = serviceInstance.getUri();
        String url = uri + "/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.getHttpBasic(clientId, clientSecret));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = responseEntity.getBody();
        if (map == null || map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //申请令牌失败
            throw new RuntimeException("申请令牌失败");
        }

        //2.封装结果数据
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken((String) map.get("access_token"));
        authToken.setRefreshToken((String) map.get("refresh_token"));
        authToken.setJti((String) map.get("jti"));

        //3.将jti作为redis中的key,将jwt作为redis中的value进行数据的存放
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(JsonUtils.toString(authToken), ttl, TimeUnit.SECONDS);
        return authToken;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public String logout(HttpServletRequest httpServletRequest) {
        //退出登录 要去redis删除jti对应的token和删除浏览器中对应的cookie
        Map<String, String> tokenObj = CookieUtil.readCookie(httpServletRequest, "uid");
        String uid = tokenObj.get("uid");
        stringRedisTemplate.delete(uid);
        return null;
    }

    private String getHttpBasic(String clientId, String clientSecret) {

        String value = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic " + new String(encode);
    }
}
