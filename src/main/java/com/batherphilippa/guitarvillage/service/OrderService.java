package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Order;
import com.batherphilippa.guitarvillage.domain.OrderDTOIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Flux<Order> findAll();
    Mono<Order> findById(String id);
    Mono<Order> save(Order order);
    Mono<Order> updateById(Mono<OrderDTOIn> order, String id);
    Mono<Void> deleteById(String id);

    Flux<Order> findByCustomerId(String customerId);
    Mono<Void> deleteByCustomerId(String customerId);
    Flux<Order> findByProductId(String productId);
}
