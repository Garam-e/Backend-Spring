package com.garam.garam_e_spring.service;


import com.garam.garam_e_spring.entity.User;
import com.garam.garam_e_spring.enums.Authority;
import com.garam.garam_e_spring.jwt.JwtTokenProvider;
import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.response.ErrorMessage;
import com.garam.garam_e_spring.user.UserRepository;
import com.garam.garam_e_spring.user.UserRequestDto;
import com.garam.garam_e_spring.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public BaseResponseDto<UserResponseDto.Register> join(UserRequestDto.Register request) {
        if (userRepository.existsUserByUserId(request.getUserId())) {
            return new BaseResponseDto<>(new UserResponseDto.Register("중복된 아이디입니다"));
        }

        User user = User.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);

        return new BaseResponseDto<>(new UserResponseDto.Register("회원가입 성공"));
    }

    @Transactional
    public BaseResponseDto<UserResponseDto.Logout> logout(UserRequestDto.Logout request) {

        if (!jwtTokenProvider.validateToken(request.getAccessToken())) {
            return new BaseResponseDto<>(ErrorMessage.TOKEN_ERROR);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());

        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        Long expiration = jwtTokenProvider.getExpiration(request.getAccessToken());
        redisTemplate.opsForValue().set(request.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        log.info("UserService.logout: logout success");
        return new BaseResponseDto<>(new UserResponseDto.Logout(true));
    }

    @Transactional
    public BaseResponseDto<UserResponseDto.UpdateInfo> update(
            String email, String newPw, String newNn) {
        User user = userRepository.findByUserId(email).orElse(null);
        if (user == null) {
            return new BaseResponseDto<>(new UserResponseDto.UpdateInfo(false, "존재하지 않는 이메일입니다."));
        }
        user.setPassword(passwordEncoder.encode(newPw));
        user.setNickname(newNn);
        userRepository.save(user);
        return new BaseResponseDto<>(new UserResponseDto.UpdateInfo(true, "정보 변경 성공"));
    }
}
