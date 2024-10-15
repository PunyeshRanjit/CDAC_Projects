package com.destina.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        String fromEmail = "your-email@gmail.com"; // Replace with actual email if not using properties

        if (fromEmail == null || fromEmail.isEmpty()) {
            throw new IllegalStateException("From email is null or empty.");
        }

        if (toEmail == null || toEmail.isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }

        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Email subject cannot be null or empty");
        }

        if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException("Email body cannot be null or empty");
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Handle messaging exception
            throw new IllegalStateException("Failed to send email due to MessagingException.", e);
        }
    }
}

