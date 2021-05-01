package com.eduardo.libraryapi.service.impl;

import com.eduardo.libraryapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.default-remetent}")
    private String remetent;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String message, List<String> emailList) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(remetent);
        mailMessage.setSubject("Livro com empréstimo atrasado");
        mailMessage.setText(message);

        String[] emails = emailList.toArray(new String[emailList.size()]);

        mailMessage.setTo(emails);

        javaMailSender.send(mailMessage);

    }
}
