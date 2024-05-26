package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SendMailCommand extends Command {

    @Setter
    private String subject;
    @Setter
    private String OTP;
    @Setter
    private String email;


    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void execute() {
        try {
            sendMail();
        } catch (Exception e) {
            ResponseFormulator.formulateException(this, e);
        }
        ResponseFormulator.formulateLogger(log, this.getState());
    }


    private void sendMail(){
        String htmlBody = readHtmlFile();
        if (htmlBody == null) {
            this.setState(new ResponseStatus(ResponseState.Failure, "Error with reading HTML file"));
            return;
        }
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
        this.setState(new ResponseStatus(ResponseState.Success, "Email sent successfully"));
    }

    private String readHtmlFile() {
        InputStream inputStream = SendMailCommand.class.getClassLoader().getResourceAsStream("templates/OTP.html");
        if (inputStream == null) {
            log.error("HTML Template not found");
            return null;
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            log.info("Reading HTML file");
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));

        }
    }
}
