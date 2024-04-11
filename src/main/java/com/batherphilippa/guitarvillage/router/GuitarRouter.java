package com.batherphilippa.guitarvillage.router;

import com.batherphilippa.guitarvillage.handler.GuitarHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class GuitarRouter {

    @Bean
    public RouterFunction<ServerResponse> guitarsRoute(GuitarHandler guitarHandler) {
        return RouterFunctions
                .route(GET("/products/guitars").and(accept(MediaType.APPLICATION_JSON)), guitarHandler::getAllGuitars)
                .andRoute(POST("/products/guitars").and(accept(MediaType.APPLICATION_JSON)), guitarHandler::createGuitar)
                .andRoute(GET("/products/guitars/{guitarId}").and(accept(MediaType.APPLICATION_JSON)), guitarHandler::getGuitarById)
                .andRoute(PUT("/products/guitars/{guitarId}").and(accept(MediaType.APPLICATION_JSON)), guitarHandler::updateGuitarById)
                .andRoute(DELETE("/products/guitars/{guitarId}").and(accept(MediaType.APPLICATION_JSON)), guitarHandler::deleteGuitarById);
    }
}
