package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    private final AccountRepository accountRepository;
    private final HolidayRequestRepository holidayRequestRepository;

    @Autowired
    public CalendarService(AccountRepository accountRepository, HolidayRequestRepository holidayRequestRepository) {
        this.accountRepository = accountRepository;
        this.holidayRequestRepository = holidayRequestRepository;
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendar(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            List<CalendarEvent> result;

            List<HolidayRequest> list = holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.accTl);
            result = foreachHolidayRequestExtractCalendarEvent(list);

            list = holidayRequestRepository.findAllByRequesterAndStatus(account, RequestStatus.accTl);

            result.addAll(foreachHolidayRequestExtractCalendarEvent(list));
            return result;
        } else
            throw new UserNotFoundException("User " + username + " was not found!");
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendarById(String accountId) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByEmployee_Id(accountId);
        if (accountOptional.isPresent())
            return loadHolidayRequestsOfTeamLeaderForCalendar(accountOptional.get().getUsername());
        else
            throw new UserNotFoundException("User was not found!");
    }

    private List<CalendarEvent> foreachHolidayRequestExtractCalendarEvent(List<HolidayRequest> list) {
        List<CalendarEvent> result = new ArrayList<>();
        String color = "black";
        for (HolidayRequest h : list) {
            switch (h.getType()) {
                case Fam:
                    color = "#E27D60";
                    break;
                case Hof:
                    color = "#85DCB";
                    break;
                case Med:
                    color = "#E8A87C";
                    break;
                case Rel:
                    color = "#C38D9E";
                    break;
            }
            result.add(new CalendarEvent(h.getId(), h.getRequester().getEmployee().getName(), h.getStart(), h.getEnd(), color));
        }
        return result;
    }
}
