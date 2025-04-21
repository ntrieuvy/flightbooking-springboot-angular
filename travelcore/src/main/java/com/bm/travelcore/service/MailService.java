package com.bm.travelcore.service;

import com.bm.travelcore.model.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface MailService {
    public void sendMail(String from, String to, String subject, String text);
    public void sendValidateMail(String to, String username, EmailTemplateName emailTemplate, String comfirmationUrl, String activationCode, String subject) throws MessagingException;


}
