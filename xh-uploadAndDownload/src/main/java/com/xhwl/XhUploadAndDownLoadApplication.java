package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author: coll man
 * @create: 2020-09-02 15:58
 */
@SpringBootApplication
public class XhUploadAndDownLoadApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhUploadAndDownLoadApplication.class,args);
    }
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
