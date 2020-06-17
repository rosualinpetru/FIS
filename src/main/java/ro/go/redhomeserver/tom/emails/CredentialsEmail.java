package ro.go.redhomeserver.tom.emails;

import lombok.Getter;
import ro.go.redhomeserver.tom.emails.EmailData;
import ro.go.redhomeserver.tom.models.Account;

@Getter
public class CredentialsEmail extends EmailData {

    public CredentialsEmail(Account to, String username, String password) {
        super(to, "Account data");

        context.setVariable("username", username);
        context.setVariable("password", password);
        context.setVariable("templateName", "credentials-email");
    }
}
