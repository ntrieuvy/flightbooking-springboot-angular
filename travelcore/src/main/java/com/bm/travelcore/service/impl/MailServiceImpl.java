package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.utils.constant.AppConstant;
import com.bm.travelcore.model.enums.EmailTemplateName;
import com.bm.travelcore.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final ApplicationProperties properties;

    @Override
    public void sendMail(String from, String to, String subject, String text) {}

    @Override
    @Async
    public void sendValidateMail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = AppConstant.TEMPLATE_CONFIRM_ACCOUNT;
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
        propertiesTemplate.put(AppConstant.TEMPLATE_PROP_USERNAME, username);
        propertiesTemplate.put(AppConstant.TEMPLATE_PROP_CONFIRMATION_URL, confirmationUrl);
        propertiesTemplate.put(AppConstant.TEMPLATE_PROP_ACTIVATION_CODE, activationCode);

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
}
