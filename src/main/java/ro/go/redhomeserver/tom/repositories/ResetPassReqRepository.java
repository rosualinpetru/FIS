package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.ResetPassReq;
import javax.transaction.Transactional;
import java.util.Date;


@Repository
public interface ResetPassReqRepository extends CrudRepository<ResetPassReq, Integer> {
    ResetPassReq findByToken(String token);
    @Transactional
    void deleteAllByAccount(Account account);
    @Transactional
    void deleteAllByExpirationDateIsLessThanEqual(Date expirationDate);
}
