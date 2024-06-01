package com.dev.assessmentservice;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AssessmentRepository extends ReactiveMongoRepository<Assessment, UUID> {
    @Query(value = "{_id: ?0}")
    @Update(update = "{$set: {type: ?1}}")
    Mono<Long> changeType(UUID id, Type type);
}
