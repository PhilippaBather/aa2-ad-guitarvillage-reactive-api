package com.batherphilippa.guitarvillage.handler;

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
    private final Validator validatorOrder;

    public OrderHandler(OrderService orderService) {
        this.orderService = orderService;
        this.validatorOrder = new OrderValidator();
    }

    public Mono<ServerResponse> getAllOrders(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.findAll(), Order.class);
    }

    public Mono<ServerResponse> getOrdersByCustomerId(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.findByCustomerId(customerId), Order.class);
    }

    public Mono<ServerResponse> getOrdersByProductId(ServerRequest serverRequest) {
        String productId = serverRequest.pathVariable("productId");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.findByProductId(productId), Order.class);
    }

    public Mono<ServerResponse> createOrder(ServerRequest serverRequest) {
        Mono<Order> newOrder = serverRequest.bodyToMono(Order.class)
                .doOnNext(this::validate);

        return newOrder.flatMap(order -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.save(order), Order.class));


//        String customerId = serverRequest.pathVariable("customerId");
//        String productId = serverRequest.pathVariable("productId");
//
//        Mono<Order> map = Mono.zip(newOrder, customerRepo.findById(customerId), guitarRepo.findById(productId))
//                .map(zipMono -> new Order(zipMono.getT1().getCreationDate(), zipMono.getT2(), zipMono.getT3(), zipMono.getT1().getPrice(), zipMono.getT1().getQuantity())).next();
//
//        map.subscribe(orderService::save);

//        return map.flatMap(order -> ServerResponse.status(HttpStatus.CREATED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(Flux.just(newOrder), OrderDTOIn.class));
    }

    public Mono<ServerResponse> getOrderById(ServerRequest serverRequest) {
        String orderId = serverRequest.pathVariable("orderId");
        return orderService.findById(orderId)
                .flatMap(o -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(o), Order.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(new ErrorResponse(NOT_FOUND.getCode(), NOT_FOUND.getMsg(), String.format("Order with id %s not found", orderId))), ErrorResponse.class));
    }


    public Mono<ServerResponse> updateOrderById(ServerRequest serverRequest) throws IllegalArgumentException {
        String orderId = serverRequest.pathVariable("orderId");
        return orderService.updateById(serverRequest.bodyToMono(OrderDTOIn.class), orderId)
                .doOnNext(this::validate)
                .flatMap(o -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(o))) // from Object deprecated
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

    public Mono<ServerResponse> deleteOrdersByCustomerId(ServerRequest serverRequest) {
        String customerId = serverRequest.pathVariable("customerId");
        return orderService.deleteByCustomerId(customerId)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    private void validate(Order order) {
        Errors errors = new BeanPropertyBindingResult(order, "order");
        validatorOrder.validate(order, errors);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getAllErrors().toString());
        }
    }

}
