package com.batherphilippa.guitarvillage.router;

import com.batherphilippa.guitarvillage.handler.OrderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class OrderRouter {

    @Bean
    public RouterFunction<ServerResponse> ordersRoute(OrderHandler orderHandler) {
        return RouterFunctions
                .route(GET("/orders").and(accept(MediaType.APPLICATION_JSON)), orderHandler::getAllOrders)
                .andRoute(POST("/orders").and(accept(MediaType.APPLICATION_JSON)), orderHandler::createOrder)
                .andRoute(GET("/orders/{orderId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::getOrderById)
                .andRoute(PUT("/orders/{orderId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::updateOrderById)
                .andRoute(DELETE("/orders/{orderId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::deleteOrderById)
                .andRoute(GET("orders/customer/{customerId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::getOrdersByCustomerId)
                .andRoute(DELETE("orders/customer/{customerId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::deleteOrdersByCustomerId)
                .andRoute(GET("orders/product/{productId}").and(accept(MediaType.APPLICATION_JSON)), orderHandler::getOrdersByProductId);

    }
}
