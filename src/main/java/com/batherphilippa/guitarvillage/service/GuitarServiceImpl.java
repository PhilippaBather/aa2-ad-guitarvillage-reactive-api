package com.batherphilippa.guitarvillage.service;

import com.batherphilippa.guitarvillage.domain.Guitar;
import com.batherphilippa.guitarvillage.respository.GuitarRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GuitarServiceImpl implements GuitarService{

    private GuitarRepository guitarRepo;

    public GuitarServiceImpl(GuitarRepository guitarRepo) {
        this.guitarRepo = guitarRepo;
    }

    @Override
    public Flux<Guitar> findAll() {
        return guitarRepo.findAll();
    }

    @Override
    public Mono<Guitar> findById(String id) {
        return guitarRepo.findById(id);
    }

    @Override
    public Mono<Guitar> save(Guitar guitar) {
        return guitarRepo.save(guitar);
    }

    @Override
    public Mono<Guitar> updateById(Mono<Guitar> guitar) {
        return guitar.flatMap(g -> guitarRepo.findById(g.getId())
                .flatMap((g1) -> {
                    g1.setProduct(g.getProduct());
                    g1.setMake(g.getMake());
                    g1.setModel(g.getModel());
                    g1.setSerialNumber(g.getSerialNumber());
                    g1.setPrice(g.getPrice());
                    g1.setType(g.getType());
                    g1.setDescription(g.getDescription());
                    return guitarRepo.save(g1);
                }));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return guitarRepo.deleteById(id);
    }
}
