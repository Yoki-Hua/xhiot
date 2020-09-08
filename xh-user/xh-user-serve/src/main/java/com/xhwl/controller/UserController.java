package com.xhwl.controller;

import com.xhwl.pojo.DTO.UserDto;
import com.xhwl.pojo.User;
import com.xhwl.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
@RequestMapping("xhwl")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "注册接口")
    @ApiResponses({
            @ApiResponse(code = 200, message="成功"),
            @ApiResponse(code = 400,message = "失败，参数错误")
    })
    public ResponseEntity<Void> register(@Valid User user, @RequestParam(required = true, value = "code") String code) {
        userService.register(user, code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/query")
    @ApiOperation(value = "登录接口")
    @ApiResponses({
            @ApiResponse(code = 200, message="成功"),
            @ApiResponse(code = 400,message = "失败，参数错误")
    })
    public ResponseEntity<UserDto> queryUserByUserNameAndPass(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {
        UserDto userDto = userService.queryUserByUserNameAndPass(userName, passWord);
        return ResponseEntity.ok(userDto);
    }
    @ApiOperation(value = "根据username查询用户数据")
    @ApiResponses({
            @ApiResponse(code = 200, message="成功"),
            @ApiResponse(code = 400,message = "失败，参数错误")
    })
    @GetMapping("/load/{username}")
    public ResponseEntity<User> queryUserByUserName(@PathVariable("username") String username){
        User user = userService.queryUserByUserName(username);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
