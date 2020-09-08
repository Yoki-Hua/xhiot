package com.xhwl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xhwl.mapper")
public class XhUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhUserApplication.class, args);
    }
}
