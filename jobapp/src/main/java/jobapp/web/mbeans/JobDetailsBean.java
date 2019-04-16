package jobapp.web.mbeans;


import jobapp.domain.models.service.JobServiceModel;
import jobapp.service.job.JobService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class JobDetailsBean {
    private JobService jobService;

    public JobDetailsBean() {
    }

    @Inject
    public JobDetailsBean(JobService jobService) {
        this.jobService = jobService;
    }

    public JobServiceModel getJobApplication(String id) {
        return this.jobService.findJobById(id);
    }
}
