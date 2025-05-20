package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.model.*;
import com.bm.travelcore.utils.constants.AppConstants;
import com.bm.travelcore.model.enums.EmailTemplateName;
import com.bm.travelcore.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final ApplicationProperties properties;

    @Override
    public void sendEmail(String from, String to, String subject, String text) {}

    @Override
    @Async
    public void sendValidateEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = AppConstants.TEMPLATE_CONFIRM_ACCOUNT;
        } else {
            templateName = emailTemplate.getName();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> propertiesTemplate = new HashMap<>();
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_USERNAME, username);
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_CONFIRMATION_URL, confirmationUrl);
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_ACTIVATION_CODE, activationCode);

        Context context = new Context();

        context.setVariables(propertiesTemplate);
        System.out.println(context);

        mimeMessageHelper.setFrom(properties.getNotReplyEmail());
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    @Override
    @Transactional
    @Async
    public void sendMailBooking(Order order) throws MessagingException {
        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            return;
        }
        Customer customer = order.getCustomer();
        List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                .peek(od -> {
                    if (od.getOrderFlightMaps() != null) {
                        od.getOrderFlightMaps().forEach(ofm -> {
                            if (ofm != null && ofm.getFlight() != null) {
                                ofm.getFlight().getDeparture();
                                ofm.getFlight().getArrival();
                            }
                        });
                    }
                })
                .collect(Collectors.toList());

        List<Passenger> passengers = order.getPassengers();
        Invoice invoice = order.getInvoices() != null && !order.getInvoices().isEmpty() ?
                order.getInvoices().get(0) : new Invoice();

        Map<String, Object> propertiesTemplate = new HashMap<>();
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_ORDER_DETAILS, orderDetails);
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_PASSENGERS, passengers);
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_CUSTOMER, customer);
        propertiesTemplate.put(AppConstants.TEMPLATE_PROP_INVOICE, invoice);

        Context context = new Context();
        context.setVariables(propertiesTemplate);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMessage, customer, orderDetails);

        String template = templateEngine.process(AppConstants.TEMPLATE_BOOKING_SUCCESS, context);
        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    private MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMessage, Customer customer, List<OrderDetail> orderDetails) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        mimeMessageHelper.setFrom(properties.getNotReplyEmail());
        mimeMessageHelper.setTo(customer.getEmail());
        String bookingCodes = orderDetails.stream().map(OrderDetail::getBookingCode).collect(Collectors.joining(", "));
        mimeMessageHelper.setSubject(
                String.format("%s - %s",
                        AppConstants.EMAIL_SUBJECT_BOOKING_SUCCESS,
                        bookingCodes)
        );
        return mimeMessageHelper;
    }
}
