package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    Department findById(int id);
}
