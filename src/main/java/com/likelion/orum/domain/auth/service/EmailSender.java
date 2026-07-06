package com.likelion.orum.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("[오름] 이메일 인증번호 안내");
        message.setText(
                "안녕하세요. 오름입니다.\n\n" +
                        "이메일 인증번호는 [" + code + "] 입니다.\n\n" +
                        "인증번호는 3분 동안 유효합니다."
        );

        javaMailSender.send(message);
    }
}
