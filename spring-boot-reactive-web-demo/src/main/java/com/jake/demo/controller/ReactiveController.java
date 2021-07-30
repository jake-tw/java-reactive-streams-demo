package com.jake.demo.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveController {

    @GetMapping(value = "/flux")
    public Flux<String> handleReqDefResult1(Model model) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(String.valueOf(i));
        }
        return Flux.fromIterable(list)
                // we have 1 sec delay to demonstrate the difference of behaviour.
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }
    
    @GetMapping("/foo")
    public Mono<String> postFooResource() {
        return Mono.just("test");
    }
}
