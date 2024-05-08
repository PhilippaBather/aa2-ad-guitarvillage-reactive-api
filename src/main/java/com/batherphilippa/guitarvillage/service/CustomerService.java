package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Customer;
import com.batherphilippa.guitarvillage.domain.CustomerDTOIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<Customer> findAll();
    Mono<Customer> findById(String id);
    Mono<Customer> save(Customer customer);
    Mono<Customer> updateById(Mono<CustomerDTOIn> customer, String id);
    Mono<Void> deleteById(String id);
}
