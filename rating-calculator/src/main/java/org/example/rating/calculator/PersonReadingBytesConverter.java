package org.example.rating.calculator;

import lombok.NoArgsConstructor;
import org.example.rating.api.Person;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
@NoArgsConstructor
public class PersonReadingBytesConverter implements Converter<byte[], Person> {
    private final Jackson2JsonRedisSerializer<Person> serializer =
            new Jackson2JsonRedisSerializer(Person.class);
    @Override
    public Person convert(final byte[] bytes) {
        return serializer.deserialize(bytes);
    }
}
