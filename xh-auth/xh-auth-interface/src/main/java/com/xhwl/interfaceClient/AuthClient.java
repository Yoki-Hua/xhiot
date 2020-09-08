package com.xhwl.interfaceClient;

import com.xhwl.pojo.AuthToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth-service")
public interface AuthClient {
    @PostMapping("/oauth/refreshToken")
    ResponseEntity<AuthToken> refreshToken(@RequestParam("grant_type") String grant_type, @RequestParam("refresh_token") String refresh_token, @RequestParam("original_jti")String original_jti);
}
