package org.example.rating.collector;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.example.rating.api.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@Profile("test")
@RunWith(CamelSpringBootRunner.class)
@UseAdviceWith
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"KAFKA_BROKER_HOST = localhost", "KAFKA_BROKER_PORT = 9092"})
public class RatingCollectorTest {

    @Autowired
    private RatingServerConfiguration serverConfig;
    @Autowired
    private RatingCollectorConfiguration consumerConfig;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:success_result")
    private MockEndpoint successResultEndpoint;

    @Before
    public void setup() throws Exception {

        camelContext.addRoutes(new TestRoutes());
        camelContext.start();
    }
    @Test
    public void testCollector() throws Exception {
        successResultEndpoint.expectedMessageCount(1);

        final Person rating = new Person("hannes", "tark", 34);
        final Gson gson = new Gson();
        final  String json = gson.toJson(rating);
        RestAssured.port = serverConfig.getPort();

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(json)
                .post("/api/rating")

        .then()
                .statusCode(200);

        successResultEndpoint.await(1L, TimeUnit.SECONDS);
        successResultEndpoint.assertIsSatisfied();
        final Exchange exc = successResultEndpoint.getReceivedExchanges().get(0);
        final String msg = exc.getIn().getBody(String.class);
        final Person received = gson.fromJson(msg, Person.class);

        assertThat(received.getAge(), is(rating.getAge()));
    }


    private class TestRoutes  extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from(consumerConfig.getTopicUrl())
                    .routeId("receiveRoute")
                    .to(successResultEndpoint);
        }
    }
}
