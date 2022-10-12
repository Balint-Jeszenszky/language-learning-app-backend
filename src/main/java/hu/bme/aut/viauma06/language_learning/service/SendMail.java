package hu.bme.aut.viauma06.language_learning.service;

import org.springframework.mail.MailException;

public interface SendMail {
    void sendSimpleMessage(String to, String subject, String text) throws MailException;
}
