package com.garam.garam_e_spring.response;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}

