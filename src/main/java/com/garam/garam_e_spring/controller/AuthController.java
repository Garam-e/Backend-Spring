package com.garam.garam_e_spring.controller;

import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.service.AuthService;
import com.garam.garam_e_spring.service.EmailService;
import com.garam.garam_e_spring.dto.UserRequestDto;
import com.garam.garam_e_spring.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/login")
    public BaseResponseDto<UserResponseDto.Login> login(
            @RequestBody UserRequestDto.Login request
    ){
        return authService.login(request.getUserId(), request.getPassword());
    }

    @PostMapping("/reissue")
    public BaseResponseDto<UserResponseDto.Reissue> reissue(
            @RequestBody UserRequestDto.Reissue token
    ){
        return authService.reissue(token);
    }

    @PostMapping("/{email}")
    public BaseResponseDto<UserResponseDto.EmailCodeCheck> sendEmail(
            @PathVariable String email
    ) throws Exception {

        return emailService.sendSimpleMessage(email);
    }

    @PatchMapping("/email")
    public BaseResponseDto<UserResponseDto.EmailCodeCheck> checkEmailCode(
            @RequestBody UserRequestDto.EmailCodeCheck request
    ) {
        return emailService.checkEmail(request.getEmail(), request.getCode());
    }
}
