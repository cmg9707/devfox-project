package com.example.demo1dww;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> list = new ArrayList<>();

    /* 텍스트 메시지 메서드 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: " + payload);
        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /* 클라이언트가 접속 요청 시 호출*/
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        log.info(session + " 클라이언트 접속 요청");
    }

    /* 클라이언트가 접속 해제 시 호출 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}

