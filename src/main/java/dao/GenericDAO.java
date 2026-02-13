package dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void delete(ID id) throws Exception;

    Optional<T> findById(ID id) throws Exception;

    List<T> findAll() throws Exception;
}
