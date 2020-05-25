package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;
import ro.go.redhomeserver.tom.dtos.WebEvent;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.HolidayReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayReqRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private HolidayReqRepository holidayReqRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> loadDelegates(Account acc_current) {
        List<Account> myTeam = accountRepository.findAllByTl(acc_current);
        if (myTeam.isEmpty()) return null;
        else {
            List<Account> colleges;
            if (acc_current.getTl() == null) {
                colleges = accountRepository.findAllByTlIsNullAndEmployee_Department(acc_current.getEmployee().getDepartment());
                colleges.remove(acc_current);
                if (colleges.size() != 0)
                    return colleges;
                else return myTeam;
            } else {
                colleges = accountRepository.findAllByTl(acc_current.getTl());
                colleges.remove(acc_current);
                colleges.add(acc_current.getTl());
                return colleges;
            }
        }

    }


    public void addRequestRecord(Account account_req, Map<String, String> params) {
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

        HolidayReq newHolidayReq = new HolidayReq(
                RequestType.valueOf(params.get("requestTypeId")),
                RequestStatus.sentTL,
                params.get("description"),
                start_date,
                end_date,
                account_req,
                accountRepository.findById(Integer.parseInt(params.get("delegateId")))
        );

        holidayReqRepository.save(newHolidayReq);

    }

    public List<WebEvent> loadHolidayReqByTl(Account tl) {
        List<HolidayReq> lst = holidayReqRepository.findAllByAccountReq_Tl(tl);
        List<WebEvent> result = new ArrayList<>();
        String color = "black";
        for (HolidayReq h : lst) {
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
            result.add(new WebEvent(h.getId(), h.getAccountReq().getEmployee().getName(), h.getStart(), h.getEnd(), color));
        }
        return result;
    }


}
