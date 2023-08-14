package com.garam.garam_e_spring.service;

import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.dto.UserResponseDto;

public interface EmailService {
    BaseResponseDto<UserResponseDto.Inquiry> sendInquiryMessage(String title, String content, String username) throws Exception;
    BaseResponseDto<UserResponseDto.EmailCodeCheck> sendSimpleMessage(String to) throws Exception;
    BaseResponseDto<UserResponseDto.EmailCodeCheck> checkEmail(String email, String code);
}
