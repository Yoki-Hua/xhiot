package com.xhwl.controller;

import com.xhwl.pojo.Email;
import com.xhwl.service.SendEmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("xhwl")
@RestController
public class SendEmailController {
    @Autowired
    private SendEmailService sendEmailService;

    @PostMapping("/send/email")
    @ApiOperation(value = "发送邮件验证码")
    @ApiResponses({
            @ApiResponse(code = 200, message="成功"),
            @ApiResponse(code = 400,message = "失败，参数错误")
    })
    public ResponseEntity<Void> sendEmail(@RequestBody(required = true) Email email) {
        sendEmailService.sendEmail(email.getEmail());
        return ResponseEntity.ok().build();
    }
}
