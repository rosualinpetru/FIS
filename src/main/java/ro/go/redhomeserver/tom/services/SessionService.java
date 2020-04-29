package ro.go.redhomeserver.tom.services;

import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.Account;

import javax.servlet.http.HttpServletRequest;

@Service
public class SessionService {
    public void addAccountSession(Account acc, HttpServletRequest request) {
        Account active = (Account) request.getSession().getAttribute("active");
        if(active == null) {
            active = acc;
            request.getSession().setAttribute("active", active);
        }
    }

    public boolean amILoggedIn(HttpServletRequest request) {
        Account active = (Account) request.getSession().getAttribute("active");
        return !(active == null);
    }
}
