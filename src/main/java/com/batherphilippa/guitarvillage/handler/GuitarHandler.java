package com.batherphilippa.guitarvillage.handler;

import com.batherphilippa.guitarvillage.domain.Guitar;
import com.batherphilippa.guitarvillage.exception.ErrorResponse;
import com.batherphilippa.guitarvillage.service.GuitarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.batherphilippa.guitarvillage.exception.ErrorType.NOT_FOUND;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class GuitarHandler {

    private GuitarService guitarService;

    public GuitarHandler(GuitarService guitarService) {
        this.guitarService = guitarService;
    }

    public Mono<ServerResponse> getAllGuitars(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(guitarService.findAll(), Guitar.class);
    }

    public Mono<ServerResponse> createGuitar(ServerRequest serverRequest) {
        Mono<Guitar> newGuitar = serverRequest.bodyToMono(Guitar.class);
        // TODO: validation
        return null;
    }

    public Mono<ServerResponse> getGuitarById(ServerRequest serverRequest) {
        String guitarId = serverRequest.pathVariable("guitarId");
        return guitarService.findById(guitarId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(g), Guitar.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Guitar with id %s not found", guitarId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> updateGuitarById(ServerRequest serverRequest) throws IllegalArgumentException {
        String guitarId = serverRequest.pathVariable("guitarId");
        return guitarService.updateById(serverRequest.bodyToMono(Guitar.class))
                // TODO: add doOnNext and validate the object received
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(g))) // from Object deprecated
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Guitar with id %s not found", guitarId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> deleteGuitarById(ServerRequest serverRequest) {
        String guitarId = serverRequest.pathVariable("guitarId");
        return guitarService.deleteById(guitarId)
                .flatMap(g -> ServerResponse.noContent().build());
    }
}
