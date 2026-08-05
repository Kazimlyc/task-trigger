package com.kamu.project.tasktrigger.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendTaskNatification(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tasktrigger@system.local");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        System.out.println("Mail başarıyla Mailtrap'e gönderildi!");
    }

}
