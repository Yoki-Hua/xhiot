package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class XhRegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhRegisterApplication.class, args);
    }
}
