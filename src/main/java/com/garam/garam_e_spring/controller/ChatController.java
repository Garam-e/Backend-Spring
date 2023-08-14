package com.garam.garam_e_spring.controller;


import com.garam.garam_e_spring.dto.ChatRequestDto;
import com.garam.garam_e_spring.dto.ChatResponseDto;
import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.response.ErrorMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final String flaskUrl = "http://localhost:5000";

    @PostMapping("/")
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
            String flaskResponse = restTemplate.postForObject(flaskUrl + "/chat", req, String.class);
            log.info("flaskResponse: {}", flaskResponse);
            return new BaseResponseDto<>(new ChatResponseDto.Chat(flaskResponse));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new BaseResponseDto<>(ErrorMessage.INTERVAL_SERVER_ERROR);
        }
    }
}
