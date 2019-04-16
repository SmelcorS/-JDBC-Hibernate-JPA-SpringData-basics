package jobapp.web.mbeans;

import jobapp.domain.models.view.JobBindingModel;
import jobapp.service.job.JobService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class JobListBean {
    private JobService jobService;

    private List<JobBindingModel> jobApplications;
    private ModelMapper modelMapper;

    public JobListBean() {
    }

    @Inject
    public JobListBean(JobService jobService, ModelMapper modelMapper) {
        this.jobService = jobService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        this.setJobApplications(this.jobService.findAllJobs()
                .stream()
                .map(jobServiceModel -> this.modelMapper
                        .map(jobServiceModel, JobBindingModel.class))
                .collect(Collectors.toList()));

        this.getJobApplications().forEach(x -> x.setSector(x.getSector().toLowerCase()));
    }

    public List<JobBindingModel> getJobApplications() {
        return this.jobApplications;
    }

    public void setJobApplications(List<JobBindingModel> jobApplications) {
        this.jobApplications = jobApplications;
    }
}
