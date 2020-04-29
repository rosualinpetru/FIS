package ro.go.redhomeserver.tom.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.thymeleaf.context.Context;

@Getter
@AllArgsConstructor
public class ResetPasswordEmail implements EmailData{
    private String resetLink;
    private String username;

    public void setContext(Context context) {
        context.setVariable("link", resetLink);
        context.setVariable("username", username);
        context.setVariable("templateName", "resetPasswordEmailTemplate");
    }

}
