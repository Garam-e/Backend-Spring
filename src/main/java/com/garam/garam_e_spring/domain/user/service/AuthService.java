package com.garam.garam_e_spring.domain.user.service;

import com.garam.garam_e_spring.global.jwt.JwtTokenProvider;
import com.garam.garam_e_spring.global.jwt.TokenInfo;
import com.garam.garam_e_spring.global.response.BaseResponseDto;
import com.garam.garam_e_spring.global.response.ErrorMessage;
import com.garam.garam_e_spring.domain.user.repository.UserRepository;
import com.garam.garam_e_spring.domain.user.dto.req.UserRequestDto;
import com.garam.garam_e_spring.domain.user.dto.res.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public BaseResponseDto<UserResponseDto.Login> login(String userId, String password) {
        if (userRepository.findByUserId(userId).orElse(null) == null) {
            return new BaseResponseDto<>(ErrorMessage.USER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken()
                , tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new BaseResponseDto<>(new UserResponseDto.Login
                (tokenInfo.getAccessToken(), tokenInfo.getRefreshToken()));
    }

    @Transactional
    public BaseResponseDto<UserResponseDto.Reissue> reissue(UserRequestDto.Reissue reissue) {
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 정보가 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return new BaseResponseDto<>(ErrorMessage.TOKEN_ERROR);
        }

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken()
                , tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new BaseResponseDto<>(new UserResponseDto.Reissue(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken()));
    }
}
