package hu.bme.aut.viauma06.language_learning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailImpl implements SendMail {
    private static final Logger logger = LoggerFactory.getLogger(SendMailImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${language_learning.app.email}")
    private String email;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) throws MailException {
        if (email.equals("DEVELOPMENT")) {
            logger.info("email: " + to + " conent: " + text);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
