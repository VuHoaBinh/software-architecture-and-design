package com.dev.receivingservice;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceivingRepository extends ReactiveMongoRepository<Receiving, UUID> {
}
