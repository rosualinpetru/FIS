package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;
import ro.go.redhomeserver.tom.repositories.ResetPasswordRequestRepository;

import java.util.Calendar;
import java.util.Date;

@Service
public class ClearDataService {

    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final HolidayRequestRepository holidayRequestRepository;

    @Autowired
    public ClearDataService(ResetPasswordRequestRepository resetPasswordRequestRepository, HolidayRequestRepository holidayRequestRepository) {
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
        this.holidayRequestRepository = holidayRequestRepository;
    }

    public void clearData() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        resetPasswordRequestRepository.deleteAllByExpirationDateIsLessThanEqual(now);
        cal.add(Calendar.YEAR, -1); // to get previous year add -1
        Date yearAgo = cal.getTime();
        holidayRequestRepository.deleteAllByStartIsLessThan(yearAgo);
    }
}
