package com.xhwl.controller;

import com.xhwl.service.AuthService;
import com.xhwl.utils.AuthToken;
import com.xhwl.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth")
//9003
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthToken> refreshToken(@RequestParam("grant_type") String grant_type, @RequestParam("refresh_token") String refresh_token, @RequestParam(name = "original_jti") String original_jti) {
        AuthToken authToken = authService.refreshToken(grant_type, refresh_token, original_jti, clientId, clientSecret);
//        this.saveJtiToCookie(authToken.getJti());
        return ResponseEntity.status(HttpStatus.OK).body(authToken);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {

        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户名不存在");
        }
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("密码不存在");
        }

        AuthToken authToken = authService.applyToken(username, password, clientId, clientSecret);

        this.saveJtiToCookie(authToken.getJti());

        return ResponseEntity.status(HttpStatus.OK).body("登录成功");

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jti = authService.logout(request);
        saveJtiToCookie(jti);
        return ResponseEntity.status(HttpStatus.OK).body("登出成功");
    }

    private void saveJtiToCookie(String jti) {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        CookieUtil.addCookie(response, cookieDomain, "/", "uid", jti, cookieMaxAge, false);
    }
}
