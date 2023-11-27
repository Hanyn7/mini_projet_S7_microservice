package com.hanin.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            emailSender.send(message);
            System.out.println("E-mail envoyé avec succès à : " + to);
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }
}
/*public void sendVerificationCodeEmail(String to, String verificationCode) throws MessagingException {
MimeMessage message = emailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(message);

try {
	helper.setTo(to);
    helper.setSubject("Verification Code");
    helper.setText("Your verification code is: " + verificationCode);

    emailSender.send(message);
    System.out.println("E-mail envoyé avec succès à : " + to);
} catch (MessagingException e) {
    System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
}
}*/