package com.feidain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.feidian.mapper")
public class ReciteWordsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReciteWordsWebApplication.class,args);
    }
}

