package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.repositories.ResetPassReqRepository;

import java.util.Date;

@Service
public class AppRefreshService {
    @Autowired
    private ResetPassReqRepository resetPassReqRepository;

    public void refreshData() {
        resetPassReqRepository.deleteAllByExpirationDateIsLessThanEqual(new Date());
    }
}
