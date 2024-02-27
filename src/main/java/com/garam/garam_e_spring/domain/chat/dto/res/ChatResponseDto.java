package com.garam.garam_e_spring.domain.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

public class ChatResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chat {
        private String answer;
        private ArrayList<String> button_name;
        private ArrayList<String> link;
        private String question;
    }
}
