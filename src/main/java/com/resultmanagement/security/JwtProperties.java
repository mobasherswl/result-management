package com.resultmanagement.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secretKey = "dummysecretkey";

    // validity in milliseconds
    private final long validityInMs = 3600000; // 1h
}
