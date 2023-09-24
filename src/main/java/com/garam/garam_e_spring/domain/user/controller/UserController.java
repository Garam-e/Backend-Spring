package com.garam.garam_e_spring.domain.user.controller;

import com.garam.garam_e_spring.domain.user.dto.req.UserRequestDto;
import com.garam.garam_e_spring.domain.user.dto.res.UserResponseDto;
import com.garam.garam_e_spring.global.jwt.JwtTokenProvider;
import com.garam.garam_e_spring.global.response.BaseResponseDto;
import com.garam.garam_e_spring.global.response.ErrorMessage;
import com.garam.garam_e_spring.domain.user.service.EmailService;
import com.garam.garam_e_spring.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public BaseResponseDto<UserResponseDto.Register> join(
            @RequestBody UserRequestDto.Register request, Errors errors
    ) {
        if(errors.hasErrors()) {
            return new BaseResponseDto<>(ErrorMessage.INVALID_FORMAT);
        }

        return userService.join(request);
    }

    @PatchMapping("/")
    public BaseResponseDto<UserResponseDto.Logout> logout(
            @RequestBody UserRequestDto.Logout request
    ) {

        return userService.logout(request);
    }

    @PatchMapping("/update")
    public BaseResponseDto<UserResponseDto.UpdateInfo> update(
            @RequestBody UserRequestDto.UpdateInfo request
    ) {
        return userService.update(
                request.getEmail(), request.getNewPassword(), request.getNewNickname());
    }

    @DeleteMapping("/")
    public BaseResponseDto<UserResponseDto.Withdraw> withdraw(
            @RequestBody UserRequestDto.Withdraw request
    ) {
        return userService.withdraw(request);
    }

    @PostMapping("/inquiry")
    public BaseResponseDto<UserResponseDto.Inquiry> inquiry(
            @RequestBody UserRequestDto.Inquiry request
    ) {
        try {
            return emailService.sendInquiryMessage(request.getTitle(), request.getContent(), request.getEmail());
        } catch (Exception e) {
            return new BaseResponseDto<>(new UserResponseDto.Inquiry(false, "메일 전송 실패"));
        }
    }

    @PatchMapping("/lang")
    public BaseResponseDto<UserResponseDto.UpdateLanguage> updateLanguage(
            @RequestBody UserRequestDto.UpdateLanguage request
    ) {
        return userService.updateLanguage(request.getLanguage(), request.getUserId());
    }

}
