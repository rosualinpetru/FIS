package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.EmailData;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailContentBuilderService emailContentBuilderService;

    @Autowired
    public EmailService(JavaMailSender mailSender, EmailContentBuilderService emailContentBuilderService){
        this.mailSender = mailSender;
        this.emailContentBuilderService = emailContentBuilderService;
    }

    @Async
    public void prepareAndSend(String recipient, EmailData data) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("tomapplication.dia@gmail.com");
            messageHelper.setTo(recipient);
            String content = emailContentBuilderService.build(data);
            messageHelper.setText(content, true);
        };
        mailSender.send(messagePreparator);
    }

}