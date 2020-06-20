package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Optional<Account> findByEmployee_Id(String id);
    Optional<Account> findByUsername(String username);
    List <Account> findAllByTeamLeaderIsNullAndEmployee_Department(Department department);
    List <Account> findAllByEmployee_Department_IdAndUsernameNot(String id, String username);
    List <Account> findAllByEmployee_Department_Id(String id);
}
