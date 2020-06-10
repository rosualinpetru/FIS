package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeService {

    private final HolidayRequestRepository holidayRequestRepository;
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(HolidayRequestRepository holidayRequestRepository, AccountRepository accountRepository, EmployeeRepository employeeRepository) {
        this.holidayRequestRepository = holidayRequestRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Account> loadPossibleDelegates(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            List<Account> myTeam = accountRepository.findAllByTeamLeader(account);
            if (myTeam.isEmpty()) return null;
            else {
                List<Account> colleagues;
                if (account.getTeamLeader() == null) {
                    colleagues = accountRepository.findAllByTeamLeaderIsNullAndEmployee_Department(account.getEmployee().getDepartment());
                    colleagues.remove(account);
                    if (colleagues.size() != 0)
                        return colleagues;
                    else return myTeam;
                } else {
                    colleagues = accountRepository.findAllByTeamLeader(account.getTeamLeader());
                    colleagues.remove(account);
                    colleagues.add(account.getTeamLeader());
                    return colleagues;
                }
            }
        } else {
            return null;
        }

    }


    public void addHolidayRequest(String username, Map<String, String> params) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Date start_date;
            Date end_date;
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                start_date = format.parse(params.get("startDate"));
                end_date = format.parse(params.get("endDate"));
            } catch (ParseException e) {
                start_date = new Date();
                end_date = new Date();
            }

            Optional<Account> delegateOptional = accountRepository.findById(Integer.parseInt(params.get("delegateId")));
            if (delegateOptional.isPresent()) {
                HolidayRequest newHolidayRequest = new HolidayRequest(
                        RequestType.valueOf(params.get("requestTypeId")),
                        RequestStatus.sentTL,
                        params.get("description"),
                        start_date,
                        end_date,
                        accountOptional.get(),
                        delegateOptional.get()
                );
                if (newHolidayRequest.getRequester().getTeamLeader() == null) {
                    newHolidayRequest.setStatus(RequestStatus.sentHR);
                }
                holidayRequestRepository.save(newHolidayRequest);
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendar(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            List<CalendarEvent> result;

            List<HolidayRequest> list = holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentHR);
            list.addAll(holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.feedHR));
            result = foreachHolidayRequestExtractCalendarEvent(list, "");

            list = holidayRequestRepository.findAllByRequesterAndStatus(account, RequestStatus.feedHR);
            list.addAll(holidayRequestRepository.findAllByRequesterAndStatus(account, RequestStatus.sentHR));

            result.addAll(foreachHolidayRequestExtractCalendarEvent(list, "ME"));
            return result;
        } else return null;
    }

    private List<CalendarEvent> foreachHolidayRequestExtractCalendarEvent(List<HolidayRequest> list, String name) {
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
            if (name.equals(""))
                name = h.getRequester().getEmployee().getName();
            result.add(new CalendarEvent(h.getId(), name, h.getStart(), h.getEnd(), color));
        }
        return result;
    }

    public List<HolidayRequest> loadPendingHolidayRequestsForATeamLeader(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.map(account -> holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentTL)).orElse(null);
    }

    public void updateStatusOfHolidayRequest(int holidayRequestId, String action) {
        Optional<HolidayRequest> requestOptional = holidayRequestRepository.findById(holidayRequestId);
        if (requestOptional.isPresent()) {
            HolidayRequest request = requestOptional.get();
            if (action.equals("acc"))
                request.setStatus(RequestStatus.sentHR);
            if (action.equals("dec"))
                request.setStatus(RequestStatus.decline);
            holidayRequestRepository.save(request);
        }
    }

    public void removeEmployee(int employeeId) {
        Optional<Employee> accountOptional = employeeRepository.findById(employeeId);
        if (accountOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
        }
    }

    public void updateTeamLeader(int employeeId1, int employeeId2) {
        Optional<Employee> employeeOptional1 = employeeRepository.findById(employeeId1);
        Optional<Employee> employeeOptional2 = employeeRepository.findById(employeeId2);
        if (employeeOptional1.isPresent() && employeeOptional2.isPresent()) {
            Account account = employeeOptional1.get().getAccount();
            account.setTeamLeader(employeeOptional2.get().getAccount());
            accountRepository.save(account);
        }
    }
}
