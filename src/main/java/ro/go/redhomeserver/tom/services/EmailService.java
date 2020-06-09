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
    public void sendEmail(EmailData data) throws MailException {
        MimeMessagePreparator messagePreparation = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("tomapplication.dia@gmail.com");
            messageHelper.setSubject(data.getSubject());
            messageHelper.setTo(data.getTo().getEmployee().getEmail());
            String content = emailContentBuilderService.build(data.getContext());
            messageHelper.setText(content, true);
        };
        mailSender.send(messagePreparation);
    }

}