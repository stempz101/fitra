package com.diploma.fitra.config.email;

import com.diploma.fitra.exception.EmailException;
import com.diploma.fitra.model.EmailUpdate;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.sender.name}")
    private String senderName;

//    @Value("${spring.mail.verification-link}")
//    private String registrationConfirmationLink;

    @Async
    public void sendRegistrationConfirmationLink(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(senderEmail, senderName));
            helper.setTo(user.getEmail());
            helper.setSubject("Registration");
            helper.setText("Hello, " + user.getFirstName() + "!\n" +
                    "Your confirmation link is " + user.getConfirmToken() + ". Please click on this link to confirm your registration.");
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(Error.FAILED_TO_SEND_REGISTRATION_CONFIRMATION_LINK.getMessage());
        }
    }

    @Async
    public void sendNewEmailConfirmationLink(User user, EmailUpdate emailUpdate) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(senderEmail, senderName));
            helper.setTo(emailUpdate.getEmail());
            helper.setSubject("Confirm your email change");
            helper.setText("Hello, " + user.getFirstName() + "!\n" +
                    "Your confirmation link is " + emailUpdate.getConfirmToken() + ". Please click on this link to confirm your new email.");
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(Error.FAILED_TO_SEND_REGISTRATION_CONFIRMATION_LINK.getMessage());
        }
    }
}
