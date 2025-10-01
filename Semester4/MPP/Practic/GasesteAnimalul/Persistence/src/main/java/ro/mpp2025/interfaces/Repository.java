package ro.mpp2025.interfaces;

import ro.mpp2025.Identity;

import java.util.Optional;

public interface Repository<ID,E extends Identity<ID>> {
    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> update(E entity);

    boolean delete(ID id);

    int size();
}
