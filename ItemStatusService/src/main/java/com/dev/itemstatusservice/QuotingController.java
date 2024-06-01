package com.dev.itemstatusservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/status")
@Slf4j
@AllArgsConstructor
@Controller
public class QuotingController {
    private ObjectMapper objectMapper;
    private QuotingStatusRepository quotingStatusRepository;

    @PostMapping("/create")
    @CrossOrigin("*")
    public Mono<ResponseEntity<Boolean>> create(@RequestBody QuotingStatusDTO info) {
        log.info("** {}", "enter status create");
        return quotingStatusRepository.save(new QuotingStatus(info))
                .flatMap(savedStatus -> {
                    log.info("** {}", "status saved successfully");
                    return Mono.just(ResponseEntity.ok(true));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("** {}", "save failed");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
                }))
                .onErrorResume(throwable -> {
                    log.error("** {}", "error occurred during save", throwable);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
                });
    }

    @GetMapping("/check-status")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> checkStatus(@RequestParam  UUID id) {
        log.info("** {}", "enter checkStatus",id);
        return quotingStatusRepository.checkStatusByIDUser(id)
                .collectList()
                .flatMap(quotingStatuses -> {
                    try {
                        return Mono.just(ResponseEntity.ok(objectMapper.writeValueAsString(quotingStatuses)));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.status(500).body("Error Json"));
                    }
                });
    }

    @GetMapping("/change-status")
    @CrossOrigin("*")
    public Mono<ResponseEntity<Boolean>> changeStatus(@RequestParam UUID id, @RequestParam Status status) {
        log.info("** {}", "enter changeStatus");
        return quotingStatusRepository.changeStatus(id,status)
                .flatMap(aLong -> {
                    if(aLong<=0) return Mono.just(ResponseEntity.status(500).body(false));
                    return Mono.just(ResponseEntity.status(200).body(true));
                });
    }

    @GetMapping("/search-by-status")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> searchByStatus() {
        log.info("** {}", "enter searchByStatus");
        return quotingStatusRepository.searchByStatus()
                .collectList()
                .flatMap(quotingStatuses -> {
                    try {
                        return Mono.just(ResponseEntity.ok(objectMapper.writeValueAsString(quotingStatuses)));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.status(500).body("Error Json"));
                    }
                });
    }

    @GetMapping("/all-statuses")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> getAllStatuses() {
        log.info("** {}", "enter getAllStatuses");
        return quotingStatusRepository.findAll()
                .collectList()
                .flatMap(quotingStatuses -> {
                    try {
                        String jsonResult = objectMapper.writeValueAsString(quotingStatuses);
                        return Mono.just(ResponseEntity.ok(jsonResult));
                    } catch (Exception e) {
                        log.error("** Error converting to JSON", e);
                        return Mono.just(ResponseEntity.status(500).body("Error Json"));
                    }
                });
    }
}
