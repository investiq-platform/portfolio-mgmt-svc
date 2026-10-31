package com.investiq.portfolio_mgmt_svc.graphql;

import com.investiq.portfolio_mgmt_svc.market.MarketDataClient.MarketDataUnavailableException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(annotations = Controller.class)
public class GraphqlExceptionHandler {

    @GraphQlExceptionHandler(MarketDataUnavailableException.class)
    GraphQLError marketDataUnavailable(MarketDataUnavailableException exception, DataFetchingEnvironment environment) {
        return graphql.GraphqlErrorBuilder.newError(environment)
                .message("Market data service is unavailable. Please try again later.")
                .extensions(Map.of("code", "MARKET_DATA_UNAVAILABLE"))
                .build();
    }

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    GraphQLError invalidRequest(IllegalArgumentException exception, DataFetchingEnvironment environment) {
        return graphql.GraphqlErrorBuilder.newError(environment)
                .message(exception.getMessage())
                .extensions(Map.of("code", "BAD_REQUEST"))
                .build();
    }
}
