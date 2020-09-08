package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringCloudApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableFeignClients
public class XhGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhGatewayApplication.class, args);
    }
}
