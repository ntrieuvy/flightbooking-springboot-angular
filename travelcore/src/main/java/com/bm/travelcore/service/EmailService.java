package com.bm.travelcore.service;

import com.bm.travelcore.model.Order;
import com.bm.travelcore.model.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
    public void sendEmail(String from, String to, String subject, String text);
    public void sendValidateEmail(String to, String username, EmailTemplateName emailTemplate, String comfirmationUrl, String activationCode, String subject) throws MessagingException;

    void sendMailBooking(Order order) throws MessagingException;
}
