package jobapp.web.mbeans;

import jobapp.domain.models.binding.UserRegisterBindingModel;
import jobapp.domain.models.service.UserServiceModel;
import jobapp.service.user.UserService;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
@RequestScoped
public class UserRegisterBean {

    private UserRegisterBindingModel userRegisterBindingModel;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserRegisterBean() {
        this.userRegisterBindingModel = new UserRegisterBindingModel();
    }

    @Inject
    public UserRegisterBean(UserService userService, ModelMapper modelMapper) {
        this();
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    public UserRegisterBindingModel getUserRegisterBindingModel() {
        return this.userRegisterBindingModel;
    }

    public void setUserRegisterBindingModel(UserRegisterBindingModel userRegisterBindingModel) {
        this.userRegisterBindingModel = userRegisterBindingModel;
    }

    public void register() throws IOException {

        this.userService.saveUser(this.modelMapper.map(this.userRegisterBindingModel, UserServiceModel.class));

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect("login.xhtml");
    }


}
