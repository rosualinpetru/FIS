package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;
import ro.go.redhomeserver.tom.exceptions.FileStorageException;
import ro.go.redhomeserver.tom.exceptions.NotEnoughDaysException;
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
    private final UploadedFileService uploadedFileService;

    @Autowired
    public EmployeeService(HolidayRequestRepository holidayRequestRepository, AccountRepository accountRepository, EmployeeRepository employeeRepository, UploadedFileService uploadedFileService) {
        this.holidayRequestRepository = holidayRequestRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.uploadedFileService = uploadedFileService;
    }

    public List<Account> loadPossibleDelegates(String username) throws UserNotFoundException {
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
        } else
            throw new UserNotFoundException("User " + username + " was not found!");
    }

    public void addHolidayRequest(String username, Map<String, String> params, MultipartFile file) throws FileStorageException, NotEnoughDaysException {
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

            Account delegate;

            if (params.get("delegateId") == null)
                delegate = null;
            else
                delegate = accountRepository.findById(params.get("delegateId")).orElse(null);

            HolidayRequest newHolidayRequest = new HolidayRequest(
                    RequestType.valueOf(params.get("requestTypeId")),
                    RequestStatus.sentTL,
                    params.get("description"),
                    start_date,
                    end_date,
                    accountOptional.get(),
                    delegate
            );
            if (newHolidayRequest.getRequester().getTeamLeader() == null) {
                newHolidayRequest.setStatus(RequestStatus.accTl);
            }

            if (newHolidayRequest.getType() == RequestType.Rel) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(newHolidayRequest.getStart());

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(newHolidayRequest.getEnd());

                int workDays = 0;

                if (startCal.getTimeInMillis() != endCal.getTimeInMillis()) {
                    do {
                        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            ++workDays;
                        }
                        startCal.add(Calendar.DAY_OF_MONTH, 1);
                    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
                }
                Account account = newHolidayRequest.getRequester();
                int remainingDays = account.getRemainingDays();
                if(remainingDays-workDays<0){
                    throw new NotEnoughDaysException("Sorry! Not enough vacation days");
                }
                account.setRemainingDays(remainingDays-workDays);
                accountRepository.save(account);
            }

            holidayRequestRepository.save(newHolidayRequest);

            if (RequestType.valueOf(params.get("requestTypeId")) == RequestType.Med) {
                try {
                    uploadedFileService.storeFile(file, newHolidayRequest);
                } catch (FileStorageException e) {
                    holidayRequestRepository.delete(newHolidayRequest);
                    throw e;
                }
            }
        }
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendar(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            List<CalendarEvent> result;

            List<HolidayRequest> list = holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.accTl);
            result = foreachHolidayRequestExtractCalendarEvent(list, "");

            list = holidayRequestRepository.findAllByRequesterAndStatus(account, RequestStatus.accTl);

            result.addAll(foreachHolidayRequestExtractCalendarEvent(list, "ME"));
            return result;
        } else
            throw new UserNotFoundException("User " + username + " was not found!");
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

    public List<HolidayRequest> loadPendingHolidayRequestsForATeamLeader(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.map(account -> holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentTL)).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " was not found!"));
    }

    public void updateStatusOfHolidayRequest(String holidayRequestId, String action) {
        Optional<HolidayRequest> requestOptional = holidayRequestRepository.findById(holidayRequestId);
        if (requestOptional.isPresent()) {
            HolidayRequest request = requestOptional.get();
            if (action.equals("acc"))
                request.setStatus(RequestStatus.accTl);

            if (action.equals("dec"))
                request.setStatus(RequestStatus.decTL);

            holidayRequestRepository.save(request);
        }
    }

    public void removeEmployee(String employeeId) {
        Optional<Employee> accountOptional = employeeRepository.findById(employeeId);
        if (accountOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
        }
    }

    public void updateTeamLeader(String employeeId1, String employeeId2) {
        Optional<Employee> employeeOptional1 = employeeRepository.findById(employeeId1);
        Optional<Employee> employeeOptional2 = employeeRepository.findById(employeeId2);
        if (employeeOptional1.isPresent() && employeeOptional2.isPresent()) {
            Account account = employeeOptional1.get().getAccount();
            account.setTeamLeader(employeeOptional2.get().getAccount());
            accountRepository.save(account);
        }
    }

    public Employee findEmployeeByUsername(String username) {
        Optional<Employee> employeeOptional = employeeRepository.findByAccount_Username(username);
        return employeeOptional.orElse(null);
    }

    public boolean isTeamLeader(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.filter(account -> !accountRepository.findAllByTeamLeader(account).isEmpty()).isPresent();
    }

    public List<CalendarEvent> loadHolidayRequestsOfTeamLeaderForCalendarById(String employeeId) throws UserNotFoundException {
        Optional<Employee> employeeOptional =employeeRepository.findById(employeeId);
        return loadHolidayRequestsOfTeamLeaderForCalendar(employeeOptional.get().getAccount().getUsername());
    }
}
