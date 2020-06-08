package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayReq;

import java.util.List;

@Repository
public interface HolidayReqRepository extends CrudRepository<HolidayReq, Integer> {
   List<HolidayReq> findAllByAccountReq_TlAndStatus(Account tl, RequestStatus status);

   HolidayReq findById(int id);
}
