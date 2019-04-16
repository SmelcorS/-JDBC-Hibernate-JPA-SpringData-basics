package jobapp.web.mbeans;

import jobapp.domain.models.binding.UserLoginBindingModel;
import jobapp.domain.models.service.UserServiceModel;
import jobapp.service.user.UserService;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Named
@RequestScoped
public class UserLoginBean {
    private UserLoginBindingModel userLoginBindingModel;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserLoginBean() {
        this.userLoginBindingModel = new UserLoginBindingModel();
    }

    @Inject
    public UserLoginBean(UserService userService, ModelMapper modelMapper) {
        this();
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    public UserLoginBindingModel getUserLoginBindingModel() {
        return this.userLoginBindingModel;
    }

    public void setUserLoginBindingModel(UserLoginBindingModel userLoginBindingModel) {
        this.userLoginBindingModel = userLoginBindingModel;
    }

    public void login() throws IOException {

        UserServiceModel userServiceModel= this.userService.userLogin(this.modelMapper.map(this.userLoginBindingModel, UserServiceModel.class));

        if (userServiceModel == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        session.setAttribute("username", userServiceModel.getUsername());

        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");

    }
}
