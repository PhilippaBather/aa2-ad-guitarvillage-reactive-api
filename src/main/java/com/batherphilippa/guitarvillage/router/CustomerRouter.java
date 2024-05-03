package com.batherphilippa.guitarvillage.router;

import com.batherphilippa.guitarvillage.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouter {

    @Bean
    public RouterFunction<ServerResponse> guitarsRoute(CustomerHandler customerHandler) {
        return RouterFunctions
                .route(GET("/customers").and(accept(MediaType.APPLICATION_JSON)), customerHandler::getAllCustomers)
                .andRoute(POST("/customers").and(accept(MediaType.APPLICATION_JSON)), customerHandler::createCustomer)
                .andRoute(GET("/customers/{customerId}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::getCustomerById)
                .andRoute(PUT("/customers/{customerId}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::updateCustomerById)
                .andRoute(DELETE("/customers/{customerId}").and(accept(MediaType.APPLICATION_JSON)), customerHandler::deleteCustomerById);
    }
}