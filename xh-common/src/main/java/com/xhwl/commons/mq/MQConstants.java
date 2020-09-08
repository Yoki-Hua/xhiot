package com.xhwl.commons.mq;

public  abstract class   MQConstants {
    public static final class Exchange {

        /**
         * 消息服务交换机名称
         */
        public static final String EMAIL_EXCHANGE_NAME = "xhwl.email.exchange";
    }

    public static final class RoutingKey {
        /**
         * 邮件发送的routing-key
         */
        public static final String VERIFY_CODE_KEY = "email.verify.code";
    }

    public static final class Queue{
        /**
         * 邮件的收发队列
         */
        public static final String EMAIL_VERIFY_CODE_QUEUE = "EMAIL.verify.code.queue";
    }
}
