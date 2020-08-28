package org.example.rating.calculator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rating")
@Getter
@Setter
public class RatingCalculatorConfiguration {
    private int port;
    private String topicUrl;
    private String redisHost;
    private double seed;
}
