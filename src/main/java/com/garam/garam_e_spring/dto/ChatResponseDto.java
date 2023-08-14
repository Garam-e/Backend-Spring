package com.garam.garam_e_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ChatResponseDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Chat {
        private String message;
    }
}
