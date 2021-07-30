package com.jake.demo.handler;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.jake.demo.annotation.WebSocketMapper;
import com.jake.demo.model.Foo;
import com.jake.demo.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@WebSocketMapper("/socket")
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

    @Autowired
    private JsonMapper jsonMapper;

    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    
    private Flux<Foo> intervalFlux = Flux.interval(Duration.ofSeconds(1))
            .map(sec -> Foo.builder().id(UUID.randomUUID().toString()).time(sec).build());

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        Flux<WebSocketMessage> flux = intervalFlux
                .map(jsonMapper::convertAsString)
                .map(webSocketSession::textMessage)
                .take(5)
                .doOnSubscribe(sig -> {
                    broadcast(webSocketSession.getId());
                    sessionMap.put(webSocketSession.getId(), webSocketSession);
                })
                .doFinally(sig -> {
                    log.info("receive single: {}", sig);
                    webSocketSession.close();
                    sessionMap.remove(webSocketSession.getId());
                });
        
        return webSocketSession.send(flux)
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .log());
    }
    
    private void broadcast(String id) {
        sessionMap.values().forEach(session -> {
            session.send(Mono.just(String.format("New session arrived: %s", id)).map(session::textMessage))
                    .subscribe();
        });
    }
}