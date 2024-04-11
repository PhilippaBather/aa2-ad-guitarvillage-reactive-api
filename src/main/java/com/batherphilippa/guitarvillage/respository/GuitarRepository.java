package com.batherphilippa.guitarvillage.respository;

import com.batherphilippa.guitarvillage.domain.Guitar;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuitarRepository extends ReactiveMongoRepository<Guitar, String> {
}
