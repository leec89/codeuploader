package com.example.codeuploader.services;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("emailService")
public class EmailService {

    @Value("${spring_sendgrid_api_key}")
    private String sendgridApiKey;

    @Value("${spring_sendgrid_email_from}")
    private String sendgridEmailFrom;

    public void prepareAndSend(String subject, String emailContent, String emailTo) {
        Email from = new Email(sendgridEmailFrom);
        Email to = new Email(emailTo);
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

        } catch(IOException mex) {
            System.out.println(mex.getMessage());
        }
    }
}
