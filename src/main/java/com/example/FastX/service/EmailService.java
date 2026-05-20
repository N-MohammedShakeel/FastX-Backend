package com.example.FastX.service;

import com.example.FastX.entity.Booking;
import com.example.FastX.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendBookingConfirmation(User user, Booking booking) throws MessagingException {

        Context context = new Context();

        context.setVariable("booking", booking);
        context.setVariable("user", user);

        String html = templateEngine.process("booking-confirmation", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("FastX Booking Confirmation");
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Async
    public void sendForgotPasswordMail(
            User user,
            String tempPassword
    ) throws MessagingException {

        Context context = new Context();

        context.setVariable(
                "name",
                user.getName()
        );

        context.setVariable(
                "password",
                tempPassword
        );

        String html =
                templateEngine.process(
                        "forgot-password",
                        context
                );

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("FastX Password Reset");
        helper.setText(html, true);

        mailSender.send(message);
    }
}