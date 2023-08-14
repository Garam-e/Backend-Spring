package com.garam.garam_e_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Reissue {
        private String accessToken;
        private String refreshToken;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Register {
        private boolean isSucceed;
        private String message;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Login {
        private String accessToken;
        private String refreshToken;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Logout {
        private boolean isSucceed;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Withdraw {
        private boolean isSucceed;
        private String message;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class IdCheck {
        private boolean isExist;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailCodeCheck {
        private boolean isSucceed;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateInfo {
        private boolean isSucceed;
        private String message;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateLanguage {
        private boolean isSucceed;
        private String message;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Inquiry {
        private boolean isSucceed;
        private String message;
    }
}
