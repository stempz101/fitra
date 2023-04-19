package com.diploma.fitra.config.email;

import com.diploma.fitra.exception.EmailException;
import com.diploma.fitra.model.EmailUpdate;
import com.diploma.fitra.model.PasswordRecoveryToken;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    @Value("${confirmation.registration-link}")
    private String registrationConfirmationLink;

    @Value("${confirmation.email-update-link}")
    private String emailConfirmationLink;

    @Async
    public void sendRegistrationConfirmationLink(User user) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(senderEmail, senderName));
            helper.setTo(user.getEmail());
            helper.setSubject("Registration");

            Context context = new Context();
            context.setVariable("name", user.getFirstName());
            context.setVariable("confirmationLink",
                    registrationConfirmationLink + "?token=" + user.getConfirmToken());
            String htmlBody = templateEngine.process("registration-confirmation-template", context);

            helper.setText(htmlBody ,true);
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
                    "Your confirmation link is " + emailConfirmationLink +
                    "?token=" + emailUpdate.getConfirmToken() + ". Please click on this link to confirm your new email.");
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(Error.FAILED_TO_SEND_REGISTRATION_CONFIRMATION_LINK.getMessage());
        }
    }

    @Async
    public void sendPasswordRecoveryLink(User user, PasswordRecoveryToken passwordRecoveryToken) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(new InternetAddress(senderEmail, senderName));
            helper.setTo(user.getEmail());
            helper.setSubject("Password reset request");
            helper.setText("Hello, " + user.getFirstName() + "!\n" +
                    "To reset your password, please click on the following link: " + passwordRecoveryToken.getToken());
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException(Error.FAILED_TO_SEND_REGISTRATION_CONFIRMATION_LINK.getMessage());
        }
    }
}
