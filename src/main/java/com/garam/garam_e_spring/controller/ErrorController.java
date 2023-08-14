package com.garam.garam_e_spring.controller;

import com.garam.garam_e_spring.response.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/error")
public class ErrorController {

    private final HttpServletRequest request;

    @GetMapping("")
    public BaseResponseDto<?> error() {
        HttpStatus status = getStatus(request);

        return new BaseResponseDto<>(status.value(), false, status.getReasonPhrase());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
