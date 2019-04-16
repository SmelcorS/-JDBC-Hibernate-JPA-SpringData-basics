package jobapp.repository.Job;

import jobapp.domain.entities.Job;
import jobapp.repository.GenericRepository;

public interface JobRepository extends GenericRepository<Job, String> {
    void deleteJobById(String id);
}
