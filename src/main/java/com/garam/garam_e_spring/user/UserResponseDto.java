package com.garam.garam_e_spring.user;

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
    public static class EmailCheck {
        private boolean isSucceed;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateInfo {
        private boolean isSucceed;
        private String message;
    }
}
