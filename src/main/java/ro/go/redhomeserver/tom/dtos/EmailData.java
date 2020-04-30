package ro.go.redhomeserver.tom.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.thymeleaf.context.Context;
import ro.go.redhomeserver.tom.models.Account;

@Getter
public abstract class EmailData {
    protected final Account to;
    protected final String subject;
    protected final Context context = new Context();

    public EmailData(Account to, String subject) {
        this.to = to;
        this.subject = subject;
    }
}
