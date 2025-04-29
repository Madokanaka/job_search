package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;

    public void sendEmail(String to, String lnk) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(EMAIL_FROM, "Job Search No Reply");
        mimeMessageHelper.setTo(to);
        String subject = "Password recovery";
        String content = "<p>Hello, </p>" +
                "<p>You have requested password reset.</p>" +
                "<p>Click on the link below to reset your password.</p>" +
                "<p><a = href=\"" + lnk + "\">Change my password </a></p>" +
                "<br>" +
                "<p> Ignore this email if you have not made the request.</p>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }
}
