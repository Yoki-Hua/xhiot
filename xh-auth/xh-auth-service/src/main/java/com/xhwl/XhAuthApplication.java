package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xhwl.mapper")
@EnableFeignClients("com.xhwl.client")
public class XhAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhAuthApplication.class);
    }

    @Bean
    RestTemplate restTemplate() {
       return new RestTemplate();
    }
}
