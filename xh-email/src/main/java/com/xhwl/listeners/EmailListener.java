package com.xhwl.listeners;

import com.xhwl.commons.exceptions.XhException;
import com.xhwl.commons.utils.JsonUtils;
import com.xhwl.utils.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Map;

import static com.xhwl.commons.mq.MQConstants.Exchange.EMAIL_EXCHANGE_NAME;
import static com.xhwl.commons.mq.MQConstants.Queue.EMAIL_VERIFY_CODE_QUEUE;
import static com.xhwl.commons.mq.MQConstants.RoutingKey.VERIFY_CODE_KEY;

@Component
@Slf4j
public class EmailListener {
    @Autowired
    private SendEmail sendEmail;
@RabbitListener(bindings = @QueueBinding(
        value =@Queue(name = EMAIL_VERIFY_CODE_QUEUE),
        exchange = @Exchange(name = EMAIL_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
        key = VERIFY_CODE_KEY
))
    public void listenVerifyCode(Map<String, String> msg) {
        if (msg == null) {
            return;
        }
        //移除邮箱，返回判断邮箱是否为空
        String email = msg.remove("email");
        if (StringUtils.isBlank(email)) {
            return;
        }
    try {
        sendEmail.sendEmail(JsonUtils.toString(msg.get("code")),email);
    } catch (XhException e) {
        log.error("发送邮件失败");

    }

}

}
