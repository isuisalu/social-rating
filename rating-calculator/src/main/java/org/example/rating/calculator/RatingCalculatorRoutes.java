package org.example.rating.calculator;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.example.rating.api.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RatingCalculatorRoutes  extends RouteBuilder {

    private static final String RATING_CALC_ROUTE_ID = "calculatorRoute";
    private static final String RATING_QUERY_ROUTE_ID = "ratingsRoute";

    @Autowired
    private RatingCalculatorConfiguration calculatorConfiguration;
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .port(calculatorConfiguration.getPort())
                .enableCORS(true)
                .component("undertow")
                .bindingMode(RestBindingMode.json);

        final GsonDataFormat inFormat = new GsonDataFormat(Person.class);

        from(calculatorConfiguration.getTopicUrl())
                .routeId(RATING_CALC_ROUTE_ID)
                .unmarshal(inFormat)
                .process("calculatorProcessor")
                .log("********************** " +
                        "${body.person.firstName} ${body.person.lastName} rating: ${body.rating}");


        rest("/api/")
                .id(RATING_QUERY_ROUTE_ID)
                .produces("application/json")
                .get("/ratings")
                .bindingMode(RestBindingMode.json)
                .to("direct:query");
        from("direct:query")
                .process("ratingsQueryProcessor");
    }
}
