package org.example.rating.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@SpringBootApplication
@EnableRedisRepositories
public class RatingCalculatorApplication {

    @Autowired
    private RatingCalculatorConfiguration config;

    public static void main(String[] args) {

        SpringApplication.run(RatingCalculatorApplication.class, args);
    }

    @Bean
    public RedisTemplate<?, ?> getRestTemplate() {
        final RedisTemplate<?, ?> template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(getJedisConnectionFactory());
        return  template;
    }
    @Bean
    public JedisConnectionFactory getJedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(config.getRedisHost());
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisConfiguration);
        return connectionFactory;
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(final PersonReadingBytesConverter readingConverter,
                                                         final PersonWritingBytesConverter redisWritingConverter,
                                                         final PersonStringReadingConverter redisReadingStringConverter,
                                                         final PersonStringWriterConverter redisWritingStringConverter) {
        return new RedisCustomConversions(Arrays.asList(readingConverter, redisWritingConverter, redisWritingStringConverter,
                redisReadingStringConverter));
    }
}
