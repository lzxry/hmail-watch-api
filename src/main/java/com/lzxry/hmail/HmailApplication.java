package com.lzxry.hmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmailApplication.class, args);
    }

}
