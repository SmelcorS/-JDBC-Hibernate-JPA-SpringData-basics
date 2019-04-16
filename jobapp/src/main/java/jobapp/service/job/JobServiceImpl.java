package jobapp.service.job;

import jobapp.domain.entities.Job;
import jobapp.domain.enums.Sector;
import jobapp.domain.models.service.JobServiceModel;
import jobapp.repository.Job.JobRepository;
import org.modelmapper.ModelMapper;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class JobServiceImpl implements JobService {

    private JobRepository jobRepository;
    private ModelMapper modelMapper;

    @Inject
    public JobServiceImpl(JobRepository jobRepository, ModelMapper modelMapper) {
        this.jobRepository = jobRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveJob(JobServiceModel jobServiceModel) {

        try {
            Job job = this.modelMapper.map(jobServiceModel, Job.class);
            job.setSector(Sector.valueOf(jobServiceModel.getSector().toUpperCase()));
            this.jobRepository.save(job);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public List<JobServiceModel> findAllJobs() {

        List<JobServiceModel> jobs = this.jobRepository.findAll()
                .stream()
                .map(job -> this.modelMapper.map(job, JobServiceModel.class))
                .collect(Collectors.toList());

        return jobs;
    }

    @Override
    public JobServiceModel findJobById(String id) {
        String id1 = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        JobServiceModel jobServiceModel = this.modelMapper.map(this.jobRepository.findById(id), JobServiceModel.class);

        return jobServiceModel;
    }


    @Override
    public void deleteJob(String id) {
        this.jobRepository.deleteJobById(id);
    }

}
