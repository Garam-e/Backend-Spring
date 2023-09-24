package com.garam.garam_e_spring.domain.exq.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ExqRequestDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {
        private String question;
        private String answer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Delete {
        private String question;
    }
}
