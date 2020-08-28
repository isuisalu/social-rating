package org.example.rating.calculator;

import lombok.NoArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.example.rating.api.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CalculatorProcessor implements Processor {
    @Autowired
    private RatingCalculatorConfiguration calculatorConfiguration;

    @Autowired
    private RatingsRepo repo;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Person person  = exchange.getIn().getBody(Person.class);
        final PersonSocialRating rating = new PersonSocialRating(person,
                person.getAge() * calculatorConfiguration.getSeed());
        repo.save(rating);
        exchange.getIn().setBody(rating);
    }
}
