package com.garam.garam_e_spring.common.controller;

import com.garam.garam_e_spring.global.response.BaseResponseDto;
import com.garam.garam_e_spring.domain.user.service.EmailService;
import com.garam.garam_e_spring.domain.user.dto.res.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @GetMapping("/api/test")
    public String test(){
        return "test";
    }

    @GetMapping("/api/test2")
    public String test2(){
        return "test2";
    }

    @PostMapping("/api/test/email")
    public BaseResponseDto<UserResponseDto.EmailCodeCheck> testEmail(
            @RequestParam String email
    ) throws Exception {

        return emailService.sendSimpleMessage(email);
    }
}
