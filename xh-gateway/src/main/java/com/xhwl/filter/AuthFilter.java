package com.xhwl.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xhwl.interfaceClient.*;
import com.xhwl.config.FilterProperties;
import com.xhwl.pojo.AuthToken;
import com.xhwl.service.AuthService;
import com.xhwl.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@EnableConfigurationProperties(FilterProperties.class)
public class AuthFilter extends ZuulFilter {
    @Autowired
    private FilterProperties filterProps;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthClient authClient;
    @Value("${xh.cookieDomain}")
    private String cookieDomain;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }
    /**
     * true过滤器生效
     * false过滤器不生效
     *
     *
     */
    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();

        HttpServletRequest request = currentContext.getRequest();

        //获取请求资源地址
        String requestURI = request.getRequestURI();

        //获取白名单  这些配置在application中
        List<String> allowPaths = filterProps.getAllowPaths();

        //如果资源白名单，出现在资源地址中，则放行，不是，则拦截
        for (String allowPath : allowPaths) {
            if (requestURI.startsWith(allowPath)){
                return false;
            }
        }

        return true;
    }
    /**
     * 校验登录状态，从cookie中获取jti的值
     * 从redis中获取token 插入header中
     * @return
     * @throws
     */
    @Override
    public Object run()  {
        //拦截后  初始化一个RequestContext
        RequestContext currentContext = null;
        currentContext  = RequestContext.getCurrentContext();
        //获取request和response
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        //由于我们存的是jti  所以取jti
        //2.从cookie中获取jti的值,如果该值不存在,说明没有token ，拒绝本次访问
        String jti = authService.getJtiFromCookie(request);
        if (StringUtils.isEmpty(jti)){

            currentContext.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody("NO TOKEN!!!");
            return null;
        }
        //因为我们往redis也存了一份  所以要通过cookie取出来的jti取redis查有没有对应的token
        //3.从redis中获取jwt的值,如果该值不存在,说明已过期，重新刷新token
        String jwt = authService.getJwtTokenFromRedis(jti);
        if (StringUtils.isEmpty(jwt)) {
            currentContext.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody("TOKEN INVALID!!!");
            return null;
        } else {
            //就算有token  也要判断token还剩多久的有效期
            Long jwtTime = authService.getJwtTimeFromRedis(jti);
            //如果jwtTime时间<30分钟（1800秒），刷新，重新拿个新的token，大于不管
            if (jwtTime < 1800) {
                String refreshToken = authService.getJwtRefreshTokenFromRedis(jti);
                ResponseEntity<AuthToken> token = authClient.refreshToken("refresh_token", refreshToken, jti);
                jwt = token.getBody().getAccessToken();
                //更新浏览器的cookie
                CookieUtil.addCookie(response,cookieDomain,"/","uid",token.getBody().getJti(),-1,false);
            }

        }

        //4.header携带令牌的信息 继续传递到微服务  微服务才能通过验证查询

        currentContext.addZuulRequestHeader("Authorization","Bearer "+jwt);
        return null;
    }
}
