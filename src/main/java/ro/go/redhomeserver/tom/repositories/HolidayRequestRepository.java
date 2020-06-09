package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;

import java.util.List;

@Repository
public interface HolidayRequestRepository extends CrudRepository<HolidayRequest, Integer> {
    List<HolidayRequest> findAllByRequester_TeamLeaderAndStatus(Account teamLeader, RequestStatus status);
}
