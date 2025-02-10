package service;

import domain.Entity;
import domain.validation.ServiceException;
import repository.Repository;
import repository.Repository2;

import java.util.Optional;

public class Service<ID,E extends Entity<ID>,R extends Repository2<ID,E>> {
    private final R repository;

    Service(R repository) {
        this.repository = repository;
    }

    public void delete(ID id) {
        Optional<E> entity = repository.delete(id);
        if(entity.isEmpty()){
            throw new ServiceException("Entity not found");
        }
        repository.delete(id);
    }

    public E findOne(ID id) {
        Optional<E> entity = repository.findOne(id);
        if(entity.isEmpty()) {
            throw new ServiceException("Entity not found");
        }
        return entity.get();
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }

    public R getRepository() {
        return repository;
    }

}
