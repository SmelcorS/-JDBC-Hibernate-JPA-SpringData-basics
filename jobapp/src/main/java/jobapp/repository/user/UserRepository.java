package jobapp.repository.user;

import jobapp.domain.entities.User;
import jobapp.repository.GenericRepository;

public interface UserRepository extends GenericRepository<User, String> {

    User findByUsername(String username);
}
