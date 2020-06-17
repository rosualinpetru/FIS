package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Employee;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findAllByDepartment_Id(String departmentId);
}
