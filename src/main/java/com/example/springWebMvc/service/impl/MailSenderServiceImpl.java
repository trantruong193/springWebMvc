package com.example.springWebMvc.service.impl;

import com.example.springWebMvc.service.MailSenderService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender mailSender;

    public MailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);
        messageHelper.setFrom("rainny193.tntn@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(message,true);
        // ClassPathResource trỏ đến resources
        // add invoice
        ClassPathResource invoice = new ClassPathResource("../../WEB-INF/data/data.docx");
        messageHelper.addAttachment("Invoice", invoice);
        // add logo
        ClassPathResource resource = new ClassPathResource("../../WEB-INF/static/img/logo/logo.png");
        messageHelper.addInline("logoImg",resource);
        this.mailSender.send(mailMessage);
    }

    @Override
    public void sendVerifyMail(String to, String subject, String message) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);
        messageHelper.setFrom("rainny193.tntn@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(message,true);
        // add logo
        ClassPathResource resource = new ClassPathResource("../../WEB-INF/static/img/logo/logo.png");
        messageHelper.addInline("logoImg",resource);
        this.mailSender.send(mailMessage);
    }
}
