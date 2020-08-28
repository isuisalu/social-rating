package org.example.rating.collector;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rating")
@Getter
@Setter
@NoArgsConstructor
public class RatingCollectorConfiguration {
    private String topicUrl;
}
