package ro.go.redhomeserver.tom.emails;

import lombok.Getter;
import ro.go.redhomeserver.tom.models.Account;

@Getter
public class HolidayStatusUpdateEmail extends EmailData {
    public HolidayStatusUpdateEmail(Account to, String action) {
        super(to, "Holiday Request Update Status");

        context.setVariable("description", "Your team leader " + action + " your request! Check the status of the request inside the application!");
        context.setVariable("templateName", "holiday-status-update-email");
    }
}
