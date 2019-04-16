package jobapp.web.mbeans;

import jobapp.domain.models.service.JobServiceModel;
import jobapp.service.job.JobService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Named
@RequestScoped
public class JobDeleteBean {
    private JobService jobService;

    public JobDeleteBean() {
    }

    @Inject
    public JobDeleteBean(JobService jobService) {
        this.jobService = jobService;
    }


    public JobServiceModel getJob(String id) {

        JobServiceModel result = this.jobService.findJobById(id);

        return result;
    }

    public void delete() throws IOException {
        String id = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("id");
        this.jobService.deleteJob(id);

        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
    }
}
