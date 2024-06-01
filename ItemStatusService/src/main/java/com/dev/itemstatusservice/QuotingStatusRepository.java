package com.dev.itemstatusservice;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface QuotingStatusRepository extends ReactiveMongoRepository<QuotingStatus, UUID> {
    @Query("{_id:?0}")
    Flux<QuotingStatus> checkStatusByIDUser( UUID id);

    @Query(value = "{_id: ?0}")
    @Update(update = "{$set: {status: ?1}}")
    Mono<Long> changeStatus(UUID id, Status status);

    @Query("{status:'SUCCESS'}")
    Flux<QuotingStatus> searchByStatus();

     @Override
    Flux<QuotingStatus> findAll();
}
