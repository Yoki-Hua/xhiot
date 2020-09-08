package com.xhwl.service.impl;

import com.xhwl.commons.enumPackage.ExceptionEnum;
import com.xhwl.commons.exceptions.XhException;
import com.xhwl.commons.utils.RegexUtils;
import com.xhwl.pojo.Email;
import com.xhwl.service.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.xhwl.commons.mq.MQConstants.Exchange.EMAIL_EXCHANGE_NAME;
import static com.xhwl.commons.mq.MQConstants.RoutingKey.VERIFY_CODE_KEY;

@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    public void sendEmail(String email) {
        if (!RegexUtils.isEmail(email)) {
            throw new XhException(ExceptionEnum.INVALID_EMAIL);
        }
        HashMap<String, String> msg = new HashMap<>();
        // 生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //放入map中
        msg.put("email", email);
        msg.put("code", code);
        try {
            //5min失效
            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis储存数据失败");
        }
        try {
            //发送消息
            amqpTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, VERIFY_CODE_KEY, msg);
        } catch (AmqpException e) {
            e.printStackTrace();
            log.error("mq失败");

        }
    }
}
