package repository;

import domain.Entity;
import domain.validator.RepoException;

import java.util.List;
import java.util.Optional;

public interface Repository<ID,E extends Entity<ID>> {
    Optional<E> findOne(ID id);

    Optional<E> save(E entity) throws RepoException;

    List<E> findAll();
}
