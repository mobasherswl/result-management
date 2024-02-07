package com.resultmanagement;

import com.resultmanagement.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class ResultManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultManagementApplication.class, args);
    }
}
