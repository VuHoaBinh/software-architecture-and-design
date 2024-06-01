package com.dev.assessmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/assessment")
@Slf4j
@AllArgsConstructor
@Controller
public class AssessmentController {
    private ObjectMapper objectMapper;
    private AssessmentRepository repository;
    WebClient.Builder builder;

    @PostMapping("/create")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> create(@RequestParam UUID id){
        log.info("** {}", "enter create");
        WebClient webClient = builder.build();

        return repository.save(new Assessment(id))
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.just(ResponseEntity.status(500).body("Save Failed"));
                }).then(Mono.empty()))
                .flatMap(assessment -> {
                    return webClient.get()
                            .uri("http://localhost:8082/api/v1/status/change-status?id="+id+"&status="+Status.VERIFYING)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .flatMap(isValidToken -> {
                                log.info("** {}", isValidToken);
                                if(!isValidToken) return Mono.just(ResponseEntity.status(500).body("Change Status Failed"));
                                try {
                                    return Mono.just(ResponseEntity.status(200).body("Success"));
                                } catch (Exception e) {
                                    return Mono.just(ResponseEntity.status(500).body("Error Json"));
                                }
                            });
                });
    }

    @GetMapping("/search")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> search(@RequestParam UUID id){
        log.info("** {} {}", "enter search in assessment",id);
        return repository.findById(id)
                .switchIfEmpty(Mono.defer(Mono::empty).then(Mono.just(new Assessment())))
                .flatMap(assessment -> {
                    if(assessment.getId()==null) return Mono.just(ResponseEntity.status(500).body("Failed save"));
                    log.info("** not null {}", assessment.getId());
                    try {
                        return Mono.just(ResponseEntity.status(200).body(objectMapper.writeValueAsString(assessment)));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.status(500).body("Error Json"));
                    }
                });
    }

    @PostMapping("/change-type")
    @CrossOrigin("*")
    public Mono<ResponseEntity<String>> changeType(@RequestParam UUID id, @RequestParam Type type){
        log.info("** {}", "enter changeType");
        WebClient webClient = builder.build();
        return repository.changeType(id,type)
                .flatMap(aLong -> {
                    Status status = Status.SUCCESS;
                    if(type.equals(Type.FAILED))
                        status = Status.FAILED;
                    return webClient.get()
                            .uri("http://localhost:8082/api/v1/status/change-status?id="+id+"&status="+status)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .flatMap(isValidToken -> {
                                log.info("** {}", isValidToken);
                                if(!isValidToken) return Mono.just(ResponseEntity.status(500).body("Change Status Failed"));
                                try {
                                    return Mono.just(ResponseEntity.status(200).body("Success"));
                                } catch (Exception e) {
                                    return Mono.just(ResponseEntity.status(500).body("Error Json "));
                                }
                            });
                });
    }

}
