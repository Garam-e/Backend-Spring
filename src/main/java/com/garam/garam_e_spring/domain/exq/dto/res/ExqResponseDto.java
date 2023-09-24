package com.garam.garam_e_spring.domain.exq.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ExqResponseDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {
        private boolean isSucceed;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Delete {
        private boolean isSucceed;
    }
}
