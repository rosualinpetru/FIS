package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface HolidayRequestRepository extends CrudRepository<HolidayRequest, String> {
    List<HolidayRequest> findAllByRequester_TeamLeaderAndStatus(Account teamLeader, RequestStatus status);
    List<HolidayRequest> findAllByRequesterAndStatus(Account account, RequestStatus status);
    List<HolidayRequest> findAllByRequester_Employee_Department_Id(String id);
    @Transactional
    void deleteAllByEndIsLessThan(Date date);
}
