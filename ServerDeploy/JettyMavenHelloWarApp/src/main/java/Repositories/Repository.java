package Repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    Optional<T> get(int id);

    //T get(T predicate);

    List<T> get();

    void add(T entity);

    void add(T... entities);

    void add(Collection<T> entities);

    void remove(T entity);

    void remove(T... entities);

    void remove(Collection<T> entities);

    /**
     * remove the entity with the given id.
     */
    void remove(int id);

//    void update(T entity);

    /**
     * remove all entities that match the given predicate.
     */
//    void remove(Predicate predicate);
}