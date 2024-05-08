package com.batherphilippa.guitarvillage.respository;

import com.batherphilippa.guitarvillage.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

    Flux<Order> findByCustomerId(String customerId);
    Mono<Void> deleteByCustomerId(String customerId);
    Flux<Order> findByProductId(String productId);

}
