package com.garam.garam_e_spring.domain.user.service;

import com.garam.garam_e_spring.global.response.BaseResponseDto;
import com.garam.garam_e_spring.domain.user.dto.res.UserResponseDto;
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
    private static final String adminEmail = "dldmstjq99@gmail.com";

    private MimeMessage createMessage(String to) throws Exception {
        ePw = createKey();
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("GARAM-E 회원가입 인증 이메일 입니다");

        String msgg = "<div style='max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif;'>";
        msgg += "<h1 style='color: #00428B; text-align: center;'>안녕하세요 가천대 챗봇 가람이입니다!!!</h1>";
        msgg += "<p style='text-align: center;'>아래 코드를 입력해주세요 :)</p>";
        msgg += "<div style='background-color: #f0f0f0; border: 1px solid #ccc; padding: 15px; text-align: center;'>";
        msgg += "<h3 style='color: #00428B;'>인증코드:</h3>";
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
    public BaseResponseDto<UserResponseDto.EmailCodeCheck> sendSimpleMessage(String to) throws Exception {

        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return new BaseResponseDto<>(new UserResponseDto.EmailCodeCheck(true));
    }

    @Transactional
    public BaseResponseDto<UserResponseDto.EmailCodeCheck> checkEmail(String email, String code) {
        String redisCode = (String) redisTemplate.opsForValue().get("EV:" + email);
        if (redisCode == null) {
            throw new IllegalArgumentException();
        }
        if (redisCode.equals(code)) {
            redisTemplate.delete("EV:" + email);
            return new BaseResponseDto<>(new UserResponseDto.EmailCodeCheck(true));
        } else {
            return new BaseResponseDto<>(new UserResponseDto.EmailCodeCheck(false));
        }
    }

    private MimeMessage createInquiryMessage(String title, String content, String username) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, adminEmail);

        message.setSubject("GARAM-E 문의 이메일 입니다");

        String msgg = "<div style='max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif;'>";
        msgg += "<h1 style='color: #00428B; text-align: center;'>문의가 접수되었습니다!</h1>";
        msgg += "<p style='text-align: center;'>아래 문의 내용을 확인해주세요 :)</p>";
        msgg += "<div style='background-color: #f0f0f0; border: 1px solid #ccc; padding: 15px; text-align: center;'>";
        msgg += "<h3 style='color: #00428B;'>문의 제목:</h3>";
        msgg += "<p style='font-size: 20px; font-weight: bold;'>" + title + "</p>";
        msgg += "<h3 style='color: #00428B;'>문의 내용:</h3>";
        msgg += "<p style='font-size: 20px; font-weight: bold;'>" + content + "</p>";
        msgg += "<h3 style='color: #00428B;'>문의자:</h3>";
        msgg += "<p style='font-size: 20px; font-weight: bold;'>" + username + "</p>";
        msgg += "</div>";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom("GARAM-E");

        return message;
    }

    @Override
    public BaseResponseDto<UserResponseDto.Inquiry> sendInquiryMessage(String title, String content, String username) throws Exception {
        MimeMessage message = createInquiryMessage(title, content, username);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
        return new BaseResponseDto<>(new UserResponseDto.Inquiry(true, "문의 전송 성공"));
    }
}
