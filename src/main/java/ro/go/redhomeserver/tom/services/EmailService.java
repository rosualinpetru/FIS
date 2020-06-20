package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import ro.go.redhomeserver.tom.emails.EmailData;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(EmailData data) throws MailException {
        MimeMessagePreparator messagePreparation = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("tomapplication.dia@gmail.com");
            messageHelper.setSubject(data.getSubject());
            messageHelper.setTo(data.getTo().getEmployee().getEmail());
            String content = templateEngine.process((String) data.getContext().getVariable("templateName"), data.getContext());
            messageHelper.setText(content, true);
        };
        mailSender.send(messagePreparation);
    }

}