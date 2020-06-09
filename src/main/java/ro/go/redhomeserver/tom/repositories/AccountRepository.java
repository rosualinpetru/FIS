package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmployee_Id(int id);
    List <Account> findAllByTeamLeader(Account tl);
    List <Account> findAllByTeamLeaderIsNullAndEmployee_Department(Department department);
}
