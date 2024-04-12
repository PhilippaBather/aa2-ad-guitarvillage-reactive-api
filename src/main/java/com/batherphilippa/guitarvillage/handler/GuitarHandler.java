package com.batherphilippa.guitarvillage.handler;

import com.batherphilippa.guitarvillage.domain.Guitar;
import com.batherphilippa.guitarvillage.domain.GuitarDTOIn;
import com.batherphilippa.guitarvillage.exception.ErrorResponse;
import com.batherphilippa.guitarvillage.service.GuitarService;
import com.batherphilippa.guitarvillage.validation.GuitarValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.batherphilippa.guitarvillage.exception.ErrorType.NOT_FOUND;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class GuitarHandler {

    private final GuitarService guitarService;
    private final Validator validator;

    public GuitarHandler(GuitarService guitarService) {
        this.guitarService = guitarService;
        this.validator = new GuitarValidator();
    }

    public Mono<ServerResponse> getAllGuitars(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(guitarService.findAll(), Guitar.class);
    }

    public Mono<ServerResponse> createGuitar(ServerRequest serverRequest) {
        Mono<Guitar> newGuitar = serverRequest.bodyToMono(Guitar.class)
                .doOnNext(this::validate);

        return newGuitar.flatMap(guitar -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(guitarService.save(guitar), Guitar.class));
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
        return guitarService.updateById(serverRequest.bodyToMono(GuitarDTOIn.class), guitarId)
                .doOnNext(this::validate)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(g))) // from Object deprecated
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Guitar with id %s not found", guitarId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> deleteGuitarById(ServerRequest serverRequest) {
        String guitarId = serverRequest.pathVariable("guitarId");
        return guitarService.findById(guitarId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(guitarService.deleteById(g.getId()), ServerResponse.noContent().build().getClass()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Guitar with id %s not found", guitarId))), ErrorResponse.class));
    }

    private void validate(Guitar guitar) {
        Errors errors = new BeanPropertyBindingResult(guitar, "guitar");
        validator.validate(guitar, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getAllErrors().toString());
        }
    }
}
