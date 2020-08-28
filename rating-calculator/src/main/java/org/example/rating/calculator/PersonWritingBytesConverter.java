package org.example.rating.calculator;

import lombok.NoArgsConstructor;
import org.example.rating.api.Person;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
@NoArgsConstructor
public class PersonWritingBytesConverter implements Converter<Person, byte[]> {
    private final Jackson2JsonRedisSerializer<Person> serializer =
            new Jackson2JsonRedisSerializer(Person.class);

    @Override
    public byte[] convert(final Person person) {
        return serializer.serialize(person);
    }
}
