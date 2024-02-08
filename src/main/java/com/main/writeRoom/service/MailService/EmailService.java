package com.main.writeRoom.service.MailService;

import com.main.writeRoom.domain.User.User;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}") String mail;


    public MimeMessage createUpdatedEmailForm(String email, User user) throws MessagingException, UnsupportedJwtException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("WriteRoom 비밀번호 재설정 안내 메일입니다.");
        String msgOfEmail="";
        msgOfEmail += "<p> " + user.getName() + "님 안녕하세요. <p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>본 이메일을 통해 writeRoom 계정의 비밀번호를 재설정할 수 있습니다.<p>";
        msgOfEmail += "<p>아래 링크를 클릭하면 비밀번호 재설정 페이지로 이동합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<a href='http://localhost:3000/myprofile/account/pw'>비밀번호 재설정으로 이동</a>";
        msgOfEmail += "<br>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>비밀번호 재설정을 통해 비밀번호가 변경되면 계정 보안을 위해 모든 기기와 브라우저에서 자동 로그인이 해제됩니다.";
        msgOfEmail += "<p>감사합니다. <p>";
        message.setFrom(mail);
        message.setText(msgOfEmail, "utf-8", "html");
        return message;
    }

    public User sendEmail(String email, User user) throws MessagingException, UnsupportedJwtException {
        MimeMessage emailForm = createUpdatedEmailForm(email, user);
        emailSender.send(emailForm);
        return user;
    }
}
