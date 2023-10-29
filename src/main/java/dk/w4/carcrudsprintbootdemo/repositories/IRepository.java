package dk.w4.carcrudsprintbootdemo.repositories;

import java.util.Optional;

public interface IRepository<T, V> {
    boolean add(T value);

    boolean delete(V id);

    boolean update(V id, T value);

    Optional<T> findById(V id);

    Iterable<T> findAll();
}
