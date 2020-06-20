package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.go.redhomeserver.tom.emails.HolidayStatusUpdateEmail;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;
import ro.go.redhomeserver.tom.exceptions.NotEnoughDaysException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.models.UploadedFile;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HolidayService {

    private final EmailService emailService;
    private final AccountRepository accountRepository;
    private final HolidayRequestRepository holidayRequestRepository;
    private final UploadedFileService uploadedFileService;

    @Autowired
    public HolidayService(EmailService emailService, AccountRepository accountRepository, HolidayRequestRepository holidayRequestRepository, UploadedFileService uploadedFileService) {
        this.emailService = emailService;
        this.accountRepository = accountRepository;
        this.holidayRequestRepository = holidayRequestRepository;
        this.uploadedFileService = uploadedFileService;
    }

    public HolidayRequest addHolidayRequest(String username, Map<String, String> params, MultipartFile file) throws IOException, NotEnoughDaysException, UserNotFoundException, ParseException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Date start_date;
            Date end_date;

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            start_date = format.parse(params.get("startDate"));
            end_date = format.parse(params.get("endDate"));

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
                Account account = newHolidayRequest.getRequester();
                int remainingDays = account.getRemainingDays();
                int workDays = newHolidayRequest.getWorkingDays();
                if (remainingDays - workDays < 0) {
                    throw new NotEnoughDaysException("Sorry! Not enough vacation days");
                }
                account.setRemainingDays(remainingDays - workDays);
                accountRepository.save(account);
            }

            if (RequestType.valueOf(params.get("requestTypeId")) == RequestType.Med) {
                uploadedFileService.storeFile(file, newHolidayRequest);
            }

            return holidayRequestRepository.save(newHolidayRequest);
        } else
            throw new UserNotFoundException("User " + username + "was not found!");
    }

    public HolidayRequest updateStatusOfHolidayRequest(String holidayRequestId, String action) throws SystemException {
        Optional<HolidayRequest> requestOptional = holidayRequestRepository.findById(holidayRequestId);
        if (requestOptional.isPresent()) {
            String actionEmail = "";
            HolidayRequest request = requestOptional.get();
            if (action.equals("acc")) {
                request.setStatus(RequestStatus.accTl);
                actionEmail = "accepted";
            }


            if (action.equals("dec")) {
                request.setStatus(RequestStatus.decTL);
                Account account = request.getRequester();
                account.setRemainingDays(account.getRemainingDays() + request.getWorkingDays());
                accountRepository.save(account);
                actionEmail = "declined";
            }

            try {
                emailService.sendEmail(new HolidayStatusUpdateEmail(request.getRequester(), actionEmail));
                return holidayRequestRepository.save(request);
            } catch (MailException e) {
                throw new SystemException("There was a problem in the system!");
            }
        }
        return null;
    }


    public UploadedFile getFileOfRequest(String holidayRequestId) {
        return uploadedFileService.getFileByRequestId(holidayRequestId);
    }

    public List<HolidayRequest> loadPendingHolidayRequestsForATeamLeader(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.map(account -> holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(account, RequestStatus.sentTL)).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " was not found!"));
    }

    public List<HolidayRequest> loadMyAcceptedHolidayRequests(String username) throws UserNotFoundException {
        return loadHolidayRequestsBasedOnUserAndStatus(username, RequestStatus.accTl);
    }

    public List<HolidayRequest> loadMyDeclinedHolidayRequests(String username) throws UserNotFoundException {
        return loadHolidayRequestsBasedOnUserAndStatus(username, RequestStatus.decTL);
    }

    public List<HolidayRequest> loadMyPendingHolidayRequests(String username) throws UserNotFoundException {
        return loadHolidayRequestsBasedOnUserAndStatus(username, RequestStatus.sentTL);
    }

    private List<HolidayRequest> loadHolidayRequestsBasedOnUserAndStatus(String username, RequestStatus status) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.map(account -> holidayRequestRepository.findAllByRequesterAndStatus(account, status)).orElseThrow(() -> new UserNotFoundException("User with username: " + username + " was not found!"));
    }
}
