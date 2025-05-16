package repository;

import domain.Identity;
import domain.validator.RepoException;

import java.util.List;
import java.util.Optional;

public interface Repository<ID,E extends Identity<ID>> {
    Optional<E> findOne(ID id);

    List<E> findAll();

    Optional<E> save(E entity) throws RepoException;

    Optional<E> update(E entity) throws RepoException;

    void delete(ID id) throws RepoException;
}
