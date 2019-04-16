package jobapp.repository.Job;

import jobapp.domain.entities.Job;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class JobRepositoryImpl implements JobRepository {

    private EntityManager entityManager;

    @Inject
    public JobRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Job> findAll() {

        List<Job> jobs = this.entityManager.createQuery("" +
                "SELECT j " +
                "FROM Job j", Job.class)
                .getResultList();

        return jobs;
    }

    @Override
    public Job findById(String id) {

        Job job = this.entityManager.createQuery("" +
                "SELECT j " +
                "FROM Job j " +
                "WHERE j.id = :id", Job.class)
                .setParameter("id", id)
                .getSingleResult();

        return job;
    }

    @Override
    public Job save(Job entity) {

        this.entityManager.getTransaction().begin();

        this.entityManager.persist(entity);

        this.entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public void deleteJobById(String id) {

        this.entityManager.getTransaction().begin();

        this.entityManager.createQuery("" +
                "DELETE " +
                "FROM Job j " +
                "WHERE j.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        this.entityManager.getTransaction().commit();

    }
}
