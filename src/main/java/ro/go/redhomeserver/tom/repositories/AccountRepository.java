package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findByUsername(String username);
    Account findById(int id);
    Account findByEmployee_Id(int id);

    List <Account> findAllByTl(Account tl);
    List <Account> findAllByTlIsNullAndEmployee_Department(Department department);



}
