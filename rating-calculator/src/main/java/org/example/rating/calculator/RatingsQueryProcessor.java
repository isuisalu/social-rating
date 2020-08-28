package org.example.rating.calculator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class RatingsQueryProcessor implements Processor {
    @Autowired
    private RatingsRepo repo;
    @Override
    public void process(Exchange exchange) throws Exception {
        Iterator<PersonSocialRating> iter =  repo.findAll().iterator();
        List<PersonSocialRating> ratings = new ArrayList<>();
        while (iter.hasNext()) {
            ratings.add(iter.next());
        }
        exchange.getIn().setBody(ratings);
    }
}
