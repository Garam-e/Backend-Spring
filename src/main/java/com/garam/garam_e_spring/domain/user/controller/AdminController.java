package com.garam.garam_e_spring.domain.user.controller;

import com.garam.garam_e_spring.domain.exq.dto.res.ExqResponseDto;
import com.garam.garam_e_spring.domain.user.service.AdminService;
import com.garam.garam_e_spring.global.response.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/exq")
    public BaseResponseDto<ExqResponseDto.Create> addQuestion(String question, String answer) {
        return adminService.addQuestion(question, answer);
    }
}
