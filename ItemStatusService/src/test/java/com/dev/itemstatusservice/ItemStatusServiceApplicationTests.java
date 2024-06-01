package com.dev.itemstatusservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class ItemStatusServiceApplicationTests {
    @Autowired
    QuotingStatusRepository quotingStatusRepository;
    @Test
    void contextLoads() {
        Flux<QuotingStatus> hi = quotingStatusRepository.checkStatusByIDUser("hi");
        hi.collectList().subscribe(statusList -> {
            for (QuotingStatus status : statusList) {
                System.out.println(status);
            }
        }, error -> {
            System.err.println("An error occurred: " + error);
        }, () -> {
            System.out.println("Completed processing QuotingStatus list");
        });
    }
}
