package jobapp.service.user;

import jobapp.domain.entities.User;
import jobapp.domain.models.service.UserServiceModel;
import jobapp.repository.user.UserRepository;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Inject
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveUser(UserServiceModel userServiceModel) {

        try {
            this.userRepository.save(this.modelMapper.map(userServiceModel, User.class));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public List<UserServiceModel> findAllUsers() {

        List<UserServiceModel> users = this.userRepository.findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());

        return users;
    }

    @Override
    public UserServiceModel userLogin(UserServiceModel userServiceModel) {
        User user = this.userRepository.findByUsername(userServiceModel.getUsername());

        if (user == null || !userServiceModel.getPassword().equals(user.getPassword())) {
            return null;
        }


        return this.modelMapper.map(user, UserServiceModel.class);
    }
}
