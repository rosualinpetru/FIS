package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueReq;

@Repository
public interface IssueReqRepository extends CrudRepository<IssueReq, Integer> {


}
