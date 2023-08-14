package com.garam.garam_e_spring.websocket;

import com.garam.garam_e_spring.entity.exq.Exq;
import com.garam.garam_e_spring.entity.exq.ExqRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ExqRepository exqRepository;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(message.getPayload().length() == 0)
            return;

        List<Exq> result = exqRepository.findByQuestionContaining(message.getPayload());

        List<String> questions = new ArrayList<>();

        if (result.isEmpty()) {
            questions.add("검색 결과가 없습니다.");
        } else {
            for (Exq exq : result) {
                questions.add(exq.getQuestion());
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(questions);

        session.sendMessage(new TextMessage(json));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
}
