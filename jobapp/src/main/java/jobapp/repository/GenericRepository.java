package jobapp.repository;

import java.util.List;

public interface GenericRepository<Entity, Id> {

    List<Entity> findAll();

    Entity findById(Id id);

    Entity save(Entity entity);
}
