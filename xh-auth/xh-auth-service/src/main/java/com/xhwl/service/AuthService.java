package com.xhwl.service;

import com.xhwl.utils.AuthToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthToken applyToken(String username, String password, String clientId, String clientSecret);

    AuthToken refreshToken(String grant_type, String refresh_token,String original_jti,String clientId, String clientSecret );

    String logout(HttpServletRequest httpServletRequest);
}
