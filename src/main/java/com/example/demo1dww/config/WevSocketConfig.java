package com.example.demo1dww.config;

import com.example.demo1dww.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor // final 필드 생성자 생성(의존성 주입)
@EnableWebSocket
public class WevSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler chatHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "ws/teamindex")
                .setAllowedOriginPatterns("*");
    }
}
