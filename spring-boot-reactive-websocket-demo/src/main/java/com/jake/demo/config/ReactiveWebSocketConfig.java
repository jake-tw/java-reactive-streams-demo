package com.jake.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.jake.demo.annotation.WebSocketMapper;

@Configuration
public class ReactiveWebSocketConfig {
    
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();

        applicationContext.getBeansWithAnnotation(WebSocketMapper.class).entrySet().forEach(entry -> {
            WebSocketMapper mapper = applicationContext.findAnnotationOnBean(entry.getKey(), WebSocketMapper.class);

            if (mapper != null && WebSocketHandler.class.isInstance(entry.getValue())) {
                map.put(mapper.endpoint(), WebSocketHandler.class.cast(entry.getValue()));
            }
        });

        return new SimpleUrlHandlerMapping(map, Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}