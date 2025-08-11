package com.marathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 马拉松报名系统主应用程序
 *
 * @author marathon
 */
@SpringBootApplication
public class MarathonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarathonApplication.class, args);
    }
}