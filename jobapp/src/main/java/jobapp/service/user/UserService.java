package jobapp.service.user;

import jobapp.domain.models.service.UserServiceModel;
import java.util.List;

public interface UserService {

    boolean saveUser(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    UserServiceModel userLogin(UserServiceModel userServiceModel);

}
