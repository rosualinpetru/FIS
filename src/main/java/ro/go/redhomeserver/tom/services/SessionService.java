package ro.go.redhomeserver.tom.services;

import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class SessionService {
    private ArrayList<Account> activeUsers;

    public void addAccountToSession(Account acc, HttpServletRequest request) {
        activeUsers = (ArrayList<Account>) request.getSession().getAttribute("activeUsers");
        if(activeUsers == null) {
            activeUsers = new ArrayList<>();
            request.getSession().setAttribute("activeUsers", activeUsers);
        }
        activeUsers.add(acc);
    }
}
