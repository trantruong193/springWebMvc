package com.example.springWebMvc.service;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendOrderEmail(String to,String subject,String message) throws MessagingException;
    void sendVerifyMail(String to,String subject,String message) throws MessagingException;
    void sendResetPasswordMail(String to,String message) throws MessagingException;

    void sendEmail(String email, String s, String mailMessage) throws MessagingException;
}

