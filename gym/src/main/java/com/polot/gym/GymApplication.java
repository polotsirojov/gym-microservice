package com.polot.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class GymApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }

}
