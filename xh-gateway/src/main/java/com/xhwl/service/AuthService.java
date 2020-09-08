package com.xhwl.service;

import com.xhwl.commons.enumPackage.ExceptionEnum;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.commons.utils.CookieUtils;
import com.xhwl.commons.utils.JsonUtils;
import com.xhwl.pojo.AuthToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //从cookie中获取jti的值
    public String getJtiFromCookie(HttpServletRequest request) {
        String jti = CookieUtils.getCookieValue(request, "uid");
        if (!StringUtils.isEmpty(jti)) {
            return jti;
        }
        return null;
    }

    //从redis中获取token
    public String getJwtTokenFromRedis(String jti) {
        try {
            String auth = stringRedisTemplate.opsForValue().get(jti);
            AuthToken authToken = JsonUtils.toBean(auth, AuthToken.class);
            return authToken.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
            throw new XhException(ExceptionEnum.CONVERSION_ERROR);
        }
    }
    //从redis中获取刷新token
    public String getJwtRefreshTokenFromRedis(String jti) {
        String auth = stringRedisTemplate.opsForValue().get(jti);
        AuthToken authToken = JsonUtils.toBean(auth, AuthToken.class);
        return authToken.getRefreshToken();
    }
    //获取token在redis中的剩余存活时间
    public Long getJwtTimeFromRedis(String jti) {
        return stringRedisTemplate.boundValueOps(jti).getExpire();
    }
}
