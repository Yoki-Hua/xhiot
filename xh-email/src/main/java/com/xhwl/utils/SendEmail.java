package com.xhwl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmail {
    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Value("${spring.mail.userName}")
    String from;

    public void sendEmail(String code,String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setSubject("[注册验证码]"); //邮件的主题
            mailMessage.setText("您的验证码："+code+";5分钟内有效!");//验证码
            mailMessage.setTo(email); //发送给谁
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            log.error("发送邮件失败!!");

        }
    }
}
