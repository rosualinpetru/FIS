package ro.go.redhomeserver.tom.dtos;

import lombok.Getter;
import ro.go.redhomeserver.tom.models.Account;

@Getter
public class ResetPasswordEmail extends EmailData{

    public ResetPasswordEmail(Account to, String link) {
        super(to, "Reset Password");
        context.setVariable("username", to.getUsername());
        context.setVariable("link", link);
        context.setVariable("templateName", "resetPasswordEmailTemplate");
    }
}
