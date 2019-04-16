package jobapp.repository.user;

import jobapp.domain.entities.User;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private EntityManager entityManager;

    @Inject
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {

        List<User> users = this.entityManager.createQuery("" +
                "SELECT u " +
                "FROM User u", User.class)
                .getResultList();

        return users;
    }

    @Override
    public User findById(String id) {

        User user = this.entityManager.createQuery("" +
                "SELECT u " +
                "FROM User u " +
                "WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();

        return user;
    }

    @Override
    public User save(User entity) {

        this.entityManager.getTransaction().begin();

        this.entityManager.persist(entity);

        this.entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public User findByUsername(String username) {

        try {
            User user = this.entityManager.createQuery("" +
                    "SELECT u " +
                    "FROM User u " +
                    "WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return user;

        } catch (Exception e) {

            return null;

        }
    }
}
