package com.batherphilippa.guitarvillage.handler;

import com.batherphilippa.guitarvillage.domain.Customer;
import com.batherphilippa.guitarvillage.domain.CustomerDTOIn;
import com.batherphilippa.guitarvillage.domain.Order;
import com.batherphilippa.guitarvillage.domain.OrderDTOIn;
import com.batherphilippa.guitarvillage.exception.ErrorResponse;
import com.batherphilippa.guitarvillage.service.OrderService;
import com.batherphilippa.guitarvillage.validation.OrderValidator;
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
public class OrderHandler {

    private final OrderService orderService;

    private final Validator validator;

    public OrderHandler(OrderService orderService) {
        this.orderService = orderService;
        this.validator = new OrderValidator();
    }

    public Mono<ServerResponse> getAllOrders(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.findAll(), Order.class);
    }

    public Mono<ServerResponse> createOrder(ServerRequest serverRequest) {
        Mono<Order> newOrder = serverRequest.bodyToMono(Order.class)
                .doOnNext(this::validate);

        return newOrder.flatMap(order -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.save(order), Order.class));
    }

    public Mono<ServerResponse> getOrderById(ServerRequest serverRequest) {
        String orderId = serverRequest.pathVariable("orderId");
        return orderService.findById(orderId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(g), Order.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Order with id %s not found", orderId))), ErrorResponse.class));
    }


    public Mono<ServerResponse> updateOrderById(ServerRequest serverRequest) throws IllegalArgumentException {
        String orderId = serverRequest.pathVariable("orderId");
        return orderService.updateById(serverRequest.bodyToMono(OrderDTOIn.class), orderId)
                .doOnNext(this::validate)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(g))) // from Object deprecated
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Order with id %s not found", orderId))), ErrorResponse.class));
    }

    public Mono<ServerResponse> deleteOrderById(ServerRequest serverRequest) {
        String orderId = serverRequest.pathVariable("orderId");
        return orderService.findById(orderId)
                .flatMap(g -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(orderService.deleteById(g.getId()), ServerResponse.noContent().build().getClass()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Order with id %s not found", orderId))), ErrorResponse.class));
    }

    private void validate(Order order) {
        Errors errors = new BeanPropertyBindingResult(order, "order");
        validator.validate(order, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getAllErrors().toString());
        }
    }
}
