package com.vicent.xoyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@ServletComponentScan
@EnableScheduling
public class XoyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(XoyoApplication.class, args);
    }

}
