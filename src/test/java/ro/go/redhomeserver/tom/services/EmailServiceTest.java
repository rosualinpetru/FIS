package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import ro.go.redhomeserver.tom.emails.CredentialsEmail;
import ro.go.redhomeserver.tom.models.Account;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void checkIfEmailIsSent() {
        CredentialsEmail ce = new CredentialsEmail(new Account(), null, null);
        emailService.sendEmail(ce);
        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }
}
