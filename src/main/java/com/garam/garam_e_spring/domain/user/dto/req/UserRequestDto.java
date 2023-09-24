package com.garam.garam_e_spring.domain.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Reissue {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Register {
        private String userId;
        private String password;
        private String nickname;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Login {
        private String userId;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Logout {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Withdraw {
        private String userId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IdCheck {
        private String userId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class EmailCodeCheck {
        private String email;
        private String code;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UpdateInfo {
        private String email;
        private String newPassword;
        private String newNickname;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Inquiry {
        private String title;
        private String content;
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UpdateLanguage {
        private String userId;
        private String language;
    }
}
