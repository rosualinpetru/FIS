package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.RequestType;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayReqRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                colleges=accountRepository.findAllByTlIsNull();
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

        Account account_tl;
        // if()

        HolidayReq newHolidayReq = new HolidayReq(
                RequestType.valueOf(params.get("requestTypeId")),
                null,
                params.get("description"),
                start_date,
                end_date,
                accountRepository.findById(account_req),
                new Account()
        );


        // holidayReqRepository.save(newHolidayReq);
        // return newEmployee.getId();
    }


}
