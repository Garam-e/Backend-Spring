package com.garam.garam_e_spring.service;

import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.user.UserResponseDto;

public interface EmailService {
    BaseResponseDto<UserResponseDto.EmailCheck> sendSimpleMessage(String to) throws Exception;
    BaseResponseDto<UserResponseDto.EmailCheck> checkEmail(String email, String code);
}
