package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.model.Customer;
import com.agmadnasfelguc.walgreensreplica.user.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMailCommand extends Command{

    private String subject;
    private String OTP;

    private Customer customerInfo;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void execute() {
        try {
            Optional<Customer> customer = customerRepository.findById("81c9cba4-f044-463e-b412-5ae7fb357b5a");
            customerInfo =  customer.orElseThrow(() -> new RuntimeException("Customer not found" ));
            System.out.println("Sending mail to: " + customerInfo.getUser().getEmail());
            sendMail();
        } catch (Exception e) {
            System.out.println("Error sending mail: " + e.getMessage());
        }
    }

    private void sendMail(){
        String htmlBody = readHtmlFile();
        assert htmlBody != null;
        htmlBody = htmlBody.replace("${firstName}", customerInfo.getFirstName());
        htmlBody = htmlBody.replace("${lastName}", customerInfo.getLastName());
        htmlBody = htmlBody.replace("${otpPurpose}", subject.toLowerCase());
        htmlBody = htmlBody.replace("${otp}", OTP);
        String finalHtmlBody = htmlBody;
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("walgreensmailer@gmail.com");
            messageHelper.setTo(customerInfo.getUser().getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(finalHtmlBody, true);
        };
        mailSender.send(messagePreparator);
    }

    private  String readHtmlFile() {
        InputStream inputStream = SendMailCommand.class.getClassLoader().getResourceAsStream("templates/OTP.html");
        if (inputStream == null) {
            return "File not found";
        } else {
            // Using BufferedReader to read InputStream into a String
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
                return null;
            }
        }
    }
}
