package com.garam.garam_e_spring.service;

import com.garam.garam_e_spring.response.BaseResponseDto;
import com.garam.garam_e_spring.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    public static String ePw = "";
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long verifyTimeout = 3 * 60 * 1000L;

    private MimeMessage createMessage(String to) throws Exception {
        ePw = createKey();
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("GARAM-E 회원가입 인증 이메일 입니다");

        String msgg = "<div style='max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif;'>";
        msgg += "<h1 style='color: #0066cc; text-align: center;'>안녕하세요 가천대 챗봇 가람이입니다!!!</h1>";
        msgg += "<p style='text-align: center;'>아래 코드를 입력해주세요 :)</p>";
        msgg += "<div style='background-color: #f0f0f0; border: 1px solid #ccc; padding: 15px; text-align: center;'>";
        msgg += "<h3 style='color: #0066cc;'>인증코드:</h3>";
        msgg += "<p style='font-size: 20px; font-weight: bold;'>" + ePw + "</p>";
        msgg += "</div>";
        msgg += "<p style='text-align: center; font-size: 16px;'>감사합니다!</p>";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom("GARAM-E");
        redisTemplate.opsForValue().set("EV:" + to, ePw, verifyTimeout, TimeUnit.MILLISECONDS);

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i=0; i<8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @Override
    public BaseResponseDto<UserResponseDto.EmailCheck> sendSimpleMessage(String to) throws Exception {

        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return new BaseResponseDto<>(new UserResponseDto.EmailCheck(true));
    }

    @Transactional
    public BaseResponseDto<UserResponseDto.EmailCheck> checkEmail(String email, String code) {
        String redisCode = (String) redisTemplate.opsForValue().get("EV:" + email);
        if (redisCode == null) {
            throw new IllegalArgumentException();
        }
        if (redisCode.equals(code)) {
            redisTemplate.delete("EV:" + email);
            return new BaseResponseDto<>(new UserResponseDto.EmailCheck(true));
        } else {
            return new BaseResponseDto<>(new UserResponseDto.EmailCheck(false));
        }
    }
}
