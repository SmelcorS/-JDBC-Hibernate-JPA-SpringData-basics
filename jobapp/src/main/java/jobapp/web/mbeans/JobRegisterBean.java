package jobapp.web.mbeans;

import jobapp.domain.models.binding.JobRegisterBindingModel;
import jobapp.domain.models.service.JobServiceModel;
import jobapp.service.job.JobService;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
@RequestScoped
public class JobRegisterBean {

    private JobRegisterBindingModel jobRegisterBindingModel;
    private JobService jobService;
    private ModelMapper modelMapper;

    public JobRegisterBean() {
        this.jobRegisterBindingModel = new JobRegisterBindingModel();
    }

    @Inject
    public JobRegisterBean(JobService jobService, ModelMapper modelMapper) {
        this();
        this.jobService = jobService;
        this.modelMapper = modelMapper;

    }

    public JobRegisterBindingModel getJobRegisterBindingModel() {
        return this.jobRegisterBindingModel;
    }

    public void setJobRegisterBindingModel(JobRegisterBindingModel jobRegisterBindingModel) {
        this.jobRegisterBindingModel = jobRegisterBindingModel;
    }

    public void register() throws IOException, IOException {

        this.jobService.saveJob(this.modelMapper.map(this.jobRegisterBindingModel, JobServiceModel.class));

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("home.xhtml");
    }
}
