package com.garam.garam_e_spring.domain.chat.controller;


import com.garam.garam_e_spring.domain.chat.dto.req.ChatRequestDto;
import com.garam.garam_e_spring.domain.chat.dto.res.ChatResponseDto;
import com.garam.garam_e_spring.global.response.BaseResponseDto;
import com.garam.garam_e_spring.global.response.ErrorMessage;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    @Value("${flask.url}")
    private String flaskUrl;

    @PostMapping("")
    public BaseResponseDto<ChatResponseDto.Chat> chat(
            @RequestBody ChatRequestDto.Chat request
    ) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Gson gson = new Gson();
            log.info(gson.toJson(request));

            HttpEntity<String> req = new HttpEntity<>(gson.toJson(request), headers);

            RestTemplate restTemplate = new RestTemplate();
            ChatResponseDto.Chat flaskResponse = restTemplate.getForObject(flaskUrl + "/get-question/" + request.getMessage(), ChatResponseDto.Chat.class, req);
            log.info("flaskResponse: {}", flaskResponse);
            return new BaseResponseDto<>(flaskResponse);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new BaseResponseDto<>(ErrorMessage.INTERVAL_SERVER_ERROR);
        }
    }
}
