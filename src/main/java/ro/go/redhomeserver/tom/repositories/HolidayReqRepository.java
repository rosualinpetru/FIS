package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.HolidayReq;

@Repository
public interface HolidayReqRepository extends CrudRepository<HolidayReq, Integer> {
}
