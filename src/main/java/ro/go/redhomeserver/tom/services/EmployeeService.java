package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;
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

    public List<Account> loadPossibleDelegates(Account account) {
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
    }


    public void addHolidayRequest(Account account, Map<String, String> params) {
        Date start_date;
        Date end_date;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            start_date = format.parse(params.get("start_date"));
            end_date = format.parse(params.get("end_date"));
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
                    account,
                    delegateOptional.get()
            );
            holidayRequestRepository.save(newHolidayRequest);
        }
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendar(Account account) {
        List<HolidayRequest> list = holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentHR);
        list.addAll(holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.feedHR));
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

    public List<HolidayRequest> loadPendingHolidayRequestsForATeamLeader(Account account) {
        return holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentTL);
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
