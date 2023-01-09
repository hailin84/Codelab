package org.alive.springlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringlabApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringlabApplication.class, args);
    }
}