package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.utils.EmailUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.web.springmvc.web_tin_tuc.utils.EmailUtils.getResetPasswordUrl;
import static com.web.springmvc.web_tin_tuc.utils.EmailUtils.getVerificationUrl;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";

    @Async
    public void sendHtmlMessage(String name, String to, String token, String template) {
        try {
            Context context = new Context();
            context.setVariable("mailName", name);
            context.setVariable("url", getVerificationUrl(host, token));
            // Render template
            String text = templateEngine.process(template, context);
            MimeMessage message = javaMailSender.createMimeMessage();
            // Config mimemessage
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromMail);
            helper.setTo(to);
            // Meaning that text is html
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendResetPasswordMessage(String name, String to, String token, String template, Integer id) {
        try {
            Context context = new Context();
            context.setVariable("mailName", name);
            context.setVariable("url", getResetPasswordUrl(host, token, id));
            // Render template
            String text = templateEngine.process(template, context);
            MimeMessage message = javaMailSender.createMimeMessage();
            // Config mimemessage
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromMail);
            helper.setTo(to);
            // Meaning that text is html
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
