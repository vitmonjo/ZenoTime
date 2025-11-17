package com.zenotime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZenoTimeReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZenoTimeReportApplication.class, args);
    }
}

