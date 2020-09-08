package com.xhwl.client;


import com.xhwl.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/xhwl/load/{username}")
    ResponseEntity<User> queryUserByUserName(@PathVariable("username") String username);
}
