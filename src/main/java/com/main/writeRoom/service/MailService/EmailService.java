package com.main.writeRoom.service.MailService;

import com.main.writeRoom.domain.User.User;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}") String mail;


    public MimeMessage ResetPwdEmailForm(String email, User user, String resetToken) throws MessagingException, UnsupportedJwtException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[WriteRoom] 비밀번호 재설정 안내 메일입니다.");
        String msgOfEmail="";
        msgOfEmail += "<p>안녕하세요. <p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 링크를 클릭하면 <u>" + user.getEmail() + "</u> 계정의 Writeroom 비밀번호를 재설정할 수 있습니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<a href='https://illustrious-starlight-6547ce.netlify.app/reset/pw/currentEmail?token=" + resetToken+ "'>비밀번호 재설정</a>";
        msgOfEmail += "<br>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>비밀번호 재설정을 통해 비밀번호가 변경되면 계정 보안을 위해 모든 기기와 브라우저에서 자동 로그인이 해제됩니다.";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>비밀번호 재설정을 요청하지 않았다면 이 이메일을 무시하셔도 됩니다.";
        msgOfEmail += "<br><br>";
        msgOfEmail += "<p>감사합니다. <p>";
        msgOfEmail += "<br><br>";
        msgOfEmail += "<p>Writeroom 팀<p>";
        message.setFrom(mail);
        message.setText(msgOfEmail, "utf-8", "html");
        return message;
    }

    public MimeMessage ResetEmailForm(String email, User user, String resetToken) throws MessagingException, UnsupportedJwtException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[WriteRoom] 이메일 변경 안내 메일입니다.");
        String msgOfEmail="";
        msgOfEmail += "<p>안녕하세요. <p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>방금 회원님의 Writeroom 계정 이메일 주소를 <u>" + user.getEmail() + "</u>에서 <u>" + email + "</u>(으)로 변경했습니다.<p>";
        msgOfEmail += "<p>회원님이 연결한 게 맞다면 이메일을 무시해도 됩니다. 아무런 조치를 취할 필요가 없습니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<b>본인이 아닌 경우: </b>";
        msgOfEmail += "<br><br>";
        msgOfEmail += "누군가 회원님의 Writeroom 계정에 액세스했을 수 있습니다. 계정 보호를 위해 비밀번호 재설정이 필요합니다.";
        msgOfEmail += "<br><br>";
        msgOfEmail += "<a href='https://illustrious-starlight-6547ce.netlify.app/reset/pw/newEmail?token=" + resetToken+ "'>비밀번호 재설정</a>";
        msgOfEmail += "<br><br>";
        msgOfEmail += "<p>Writeroom 팀<p>";
        message.setFrom(mail);
        message.setText(msgOfEmail, "utf-8", "html");
        return message;
    }

    public User sendEmail(String email, User user, String resetToken, String type) throws MessagingException, UnsupportedJwtException {
        if (Objects.equals(type, "password")) {
            MimeMessage resetPwdEmailForm = ResetPwdEmailForm(email, user, resetToken);
            emailSender.send(resetPwdEmailForm);
        } else {
            MimeMessage resetEmailForm = ResetEmailForm(email, user, resetToken);
            emailSender.send(resetEmailForm);
        }
        return user;
    }
}
