package com.example.springWebMvc.service;

import javax.mail.MessagingException;

public interface MailSenderService {
    void sendEmail(String to,String subject,String message) throws MessagingException;
    void sendVerifyMail(String to,String subject,String message) throws MessagingException;

}
