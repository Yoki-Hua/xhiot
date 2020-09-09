package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 云上环境
 */
@SpringCloudApplication
@EnableEurekaClient
@EnableZuulProxy
public class XhZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhZuulApplication.class, args);
    }
}
