package com.dev.receivingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/receiving")
@Slf4j
@AllArgsConstructor
@Controller
public class ReceivingController {
    private ObjectMapper objectMapper;
    private ReceivingRepository repository;
    WebClient.Builder builder;

    @PostMapping("/create")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> create(@RequestBody ReceivingDTO info){
        log.info("** {}", "enter create");
        WebClient webClient = builder.build();

        return repository.save(new Receiving(info))
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.just(ResponseEntity.status(500).body("Failed save"));
                }).then(Mono.empty()))
                .flatMap(receiving -> {
                    return webClient.get()
                            .uri("http://localhost:8082/api/v1/status/change-status?id="+info.getId()+"&status="+Status.SHIPPED)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .flatMap(isValidToken -> {
                                log.info("** {}", isValidToken);
                                if(!isValidToken) return Mono.just(ResponseEntity.status(500).body("Change Status Failed"));
                                try {
                                    return Mono.just(ResponseEntity.status(200).body(objectMapper.writeValueAsString(receiving)));
                                } catch (Exception e) {
                                    return Mono.just(ResponseEntity.status(500).body("Error Json"));
                                }
                            });
                });
    }

    @GetMapping("/search")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> search(@RequestParam UUID id){
        log.info("** {} {}", "enter search",id);
        return repository.findById(id)
                .switchIfEmpty(Mono.defer(()->{
                    log.error("** findById");
                    return Mono.just(ResponseEntity.status(500).body("Failed save"));
                }).then(Mono.empty()))
                .flatMap(receiving -> {
                    try {
                        return Mono.just(ResponseEntity.status(200).body(objectMapper.writeValueAsString(receiving)));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.status(500).body("Error Json"));
                    }
                });
    }
}
