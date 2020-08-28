package org.example.rating.collector;


import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class RatingCollectorRoutes extends RouteBuilder {

    public static final String RATING_CONSUME_ROUTE_ID = "rating-consum";

    @Autowired
    private RatingServerConfiguration config;
    @Autowired
    private RatingCollectorConfiguration consumerConfig;

    public void configure() {

        restConfiguration()
                .port(config.getPort())
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiContextRouteId("doc-api")
                .component("undertow")
                .bindingMode(RestBindingMode.json);

        rest("/api/")
                .id(RATING_CONSUME_ROUTE_ID)
                .consumes("application/json")
                .post("/rating")
                .bindingMode(RestBindingMode.json)
                .to(consumerConfig.getTopicUrl());
    }
}
