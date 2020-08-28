package org.example.rating.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rating.api.Person;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ReadingConverter
@Slf4j
@NoArgsConstructor
public class PersonStringReadingConverter implements Converter<String, Person> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Person convert(final String source) {
        try {
            return objectMapper.readValue(source, Person.class);
        } catch (IOException e) {
            log.warn("Error while converting to Person.", e);
            throw new IllegalArgumentException("Can not convert to Person");
        }
    }
}
