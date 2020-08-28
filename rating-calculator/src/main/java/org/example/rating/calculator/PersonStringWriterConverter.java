package org.example.rating.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rating.api.Person;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WritingConverter
@Slf4j
@NoArgsConstructor
public class PersonStringWriterConverter implements Converter<Person, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(final Person source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (IOException e) {
            log.warn("Error while converting to Person.", e);
            throw new IllegalArgumentException("Can not convert to Person");
        }
    }
}
