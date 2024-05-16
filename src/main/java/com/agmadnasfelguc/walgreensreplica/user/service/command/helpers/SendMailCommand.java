package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class SendMailCommand extends Command {

    private String subject;
    private String OTP;
    private boolean emailSent = false;
    private String email;
    private String firstName;
    private String lastName;


    @Autowired
    private JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(SendMailCommand.class);

    @Override
    public void execute() {
        try {
            sendMail();
            if(emailSent) {
                this.setState(new ResponseStatus(ResponseState.Success, "Email sent successfully"));
                logger.info("Email sent successfully");
            } else {
                this.setState(new ResponseStatus(ResponseState.Failure, "Could not open HTML file"));
                logger.error("Could not open HTML file");
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(e.getMessage());
        }
    }

    private void sendMail(){
        String htmlBody = readHtmlFile();
        if(htmlBody == null) {
            logger.error("Html body is null");
            return;
        }
        htmlBody = htmlBody.replace("${firstName}", firstName);
        htmlBody = htmlBody.replace("${lastName}", lastName);
        htmlBody = htmlBody.replace("${otpPurpose}", subject.toLowerCase());
        htmlBody = htmlBody.replace("${otp}", OTP);
        String finalHtmlBody = htmlBody;
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("walgreensmailer@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(finalHtmlBody, true);
        };
        mailSender.send(messagePreparator);
        emailSent = true;
    }

    private String readHtmlFile() {
        InputStream inputStream = SendMailCommand.class.getClassLoader().getResourceAsStream("templates/OTP.html");
        if (inputStream == null) {
            logger.error("Template not found");
            return null;
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            logger.info("Reading HTML file");
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));

        }
    }
}
