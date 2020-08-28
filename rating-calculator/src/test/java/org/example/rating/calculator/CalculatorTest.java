package org.example.rating.calculator;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.example.rating.api.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Profile("test")
@RunWith(CamelSpringBootRunner.class)
@UseAdviceWith
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"KAFKA_BROKER_HOST = localhost",
                "KAFKA_BROKER_PORT = 9092",
                "REDIS_HOST = localhost"})
public class CalculatorTest {
    @Autowired
    private RatingCalculatorConfiguration calculatorConfig;
    @Autowired
    private RatingsRepo repo;

    @EndpointInject("direct:send")
    private ProducerTemplate sender;

    @Autowired
    private CamelContext camelContext;

    @Before
    public void setup() throws Exception {

        camelContext.addRoutes(new TestRoutes());
        camelContext.start();
    }
    @Test
    public void testCalculator() throws Exception {
        final Person person = newPerson();
        final Gson gson = new Gson();
        final String json = gson.toJson(person);

        sender.sendBody(json);
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        final PersonSocialRating savedRating = getRepoResult(repo);
        assertThat(savedRating.getRating(), is(3.4000000000000004));
    }
    @Test
    public void testRatingsQuery() {
        RestAssured.port = calculatorConfig.getPort();
        final Person person = newPerson();
        final PersonSocialRating rating = new PersonSocialRating(person, 4.5);

        repo.save(rating);

        ExtractableResponse response = given()
                .get("/api/ratings")
                .then()
                .extract();
        String json = response.asString();
        final Gson gson = new Gson();

        PersonSocialRating [] ratings = gson.fromJson(json, PersonSocialRating[].class);

        assertThat(ratings[0].getRating(), is(rating.getRating()));
    }
    private PersonSocialRating getRepoResult(final RatingsRepo repo) {
        final Iterator<PersonSocialRating> it = repo.findAll().iterator();
        PersonSocialRating savedRating = null;
        while(it.hasNext()) {
            savedRating = it.next();
        }
        return savedRating;
    }

    private Person newPerson() {
        return new Person("hannes", "tark", 34);
    }

    private class TestRoutes  extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("direct:send").routeId("topicRoute")
            .to(calculatorConfig.getTopicUrl());
       }
    }
}
