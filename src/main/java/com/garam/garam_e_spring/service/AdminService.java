package com.garam.garam_e_spring.service;

import com.garam.garam_e_spring.entity.exq.Exq;
import com.garam.garam_e_spring.entity.exq.ExqRepository;
import com.garam.garam_e_spring.dto.ExqResponseDto;
import com.garam.garam_e_spring.response.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final ExqRepository exqRepository;

    @Transactional
    public BaseResponseDto<ExqResponseDto.Create> addQuestion(String question, String answer) {
        Exq newQuestion = Exq.builder()
                .question(question)
                .answer(answer)
                .build();

        exqRepository.save(newQuestion);

        return new BaseResponseDto<>(new ExqResponseDto.Create(true));
    }
}
