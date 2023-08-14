package com.garam.garam_e_spring.service;


import com.garam.garam_e_spring.entity.user.User;
import com.garam.garam_e_spring.entity.user.UserRepository;
import com.garam.garam_e_spring.dto.UserRequestDto;
import com.garam.garam_e_spring.dto.UserResponseDto;
import com.garam.garam_e_spring.enums.Authority;
import com.garam.garam_e_spring.enums.Language;
import com.garam.garam_e_spring.jwt.JwtTokenProvider;
import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.response.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
    private final List<String> languages = List.of("KOR", "ENG", "CHN");


    @Transactional
    public BaseResponseDto<UserResponseDto.Register> join(UserRequestDto.Register request) {
        if (userRepository.existsUserByUserId(request.getUserId())) {
            return new BaseResponseDto<>(new UserResponseDto.Register(false, "중복된 아이디입니다"));
        }

        User user = User.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .language(Language.KOR.name())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);

        return new BaseResponseDto<>(new UserResponseDto.Register(true, "회원가입 성공"));
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
    public BaseResponseDto<UserResponseDto.Withdraw> withdraw(UserRequestDto.Withdraw request) {
        String userId = request.getUserId();
        if(!userRepository.existsUserByUserId(userId)) {
            return new BaseResponseDto<>(new UserResponseDto.Withdraw(false, "존재하지 않는 유저입니다."));
        }

        if (redisTemplate.opsForValue().get("RT:" + userId) != null) {
            redisTemplate.delete("RT:" + userId);
        }

        userRepository.deleteByUserId(userId);
        return new BaseResponseDto<>(new UserResponseDto.Withdraw(true, "회원탈퇴 성공"));
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

    @Transactional
    public BaseResponseDto<UserResponseDto.UpdateLanguage> updateLanguage(
            String language, String userId) {
        if(!languages.contains(language)) {
            return new BaseResponseDto<>(new UserResponseDto.UpdateLanguage(false, "존재하지 않는 언어입니다."));
        }

        userRepository.findByUserId(userId).ifPresentOrElse(user -> {
            user.setLanguage(language);
            userRepository.save(user);
        }, () -> {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        });
        return new BaseResponseDto<>(new UserResponseDto.UpdateLanguage(true, "언어 변경 성공"));
    }
}
