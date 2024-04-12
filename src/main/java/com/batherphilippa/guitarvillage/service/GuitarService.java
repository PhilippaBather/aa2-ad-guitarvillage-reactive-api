package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Guitar;
import com.batherphilippa.guitarvillage.domain.GuitarDTOIn;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuitarService {

    Flux<Guitar> findAll();
    Mono<Guitar> findById(String id);
    Mono<Guitar> save(Guitar guitar);
    Mono<Guitar> updateById(Mono<GuitarDTOIn> guitar, String id);
    Mono<Void> deleteById(String id);

}
