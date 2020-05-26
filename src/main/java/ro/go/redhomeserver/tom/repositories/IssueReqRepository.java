package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueReq;

import java.util.List;

@Repository
public interface IssueReqRepository extends CrudRepository <IssueReq, Integer> {
  List<IssueReq> findAll();
  void deleteById( int id );
}

