package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.ResetPasswordRequest;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;


@Repository
public interface ResetPasswordRequestRepository extends CrudRepository<ResetPasswordRequest, Integer> {
    Optional<ResetPasswordRequest> findByToken(String token);
    @Transactional
    void deleteAllByAccount(Account account);
    @Transactional
    void deleteAllByExpirationDateIsLessThanEqual(Date expirationDate);
}
