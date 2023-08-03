package com.garam.garam_e_spring.controller;

import com.garam.garam_e_spring.jwt.JwtTokenProvider;
import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.entity.User;
import com.garam.garam_e_spring.response.ErrorMessage;
import com.garam.garam_e_spring.user.UserRequestDto;
import com.garam.garam_e_spring.user.UserResponseDto;
import com.garam.garam_e_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
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

    @PatchMapping("/logout")
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
}
