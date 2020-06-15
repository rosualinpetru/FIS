package ro.go.redhomeserver.tom.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.go.redhomeserver.tom.models.UploadedFile;

@Repository
public interface UploadedFileRepository extends CrudRepository<UploadedFile, String> {
}
