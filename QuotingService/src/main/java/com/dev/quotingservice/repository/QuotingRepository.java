package com.dev.quotingservice.repository;

import com.dev.quotingservice.entity.Quoting;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuotingRepository extends ReactiveMongoRepository<Quoting, UUID> {
}
