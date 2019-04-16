package jobapp.service.job;

import jobapp.domain.entities.Job;
import jobapp.domain.models.service.JobServiceModel;

import java.util.List;

public interface JobService {

    boolean saveJob(JobServiceModel jobServiceModel);

    List<JobServiceModel> findAllJobs();

    JobServiceModel findJobById(String id);

    void deleteJob(String id);
}
