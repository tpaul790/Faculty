package repository.memory;

import domain.Entity;
import domain.User  ;
import domain.validation.Validator;
import repository.Repository;
import repository.Repository2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryRepo<ID,E extends Entity<ID>> implements Repository2<ID,E> {
    Validator<E> validator;
    private Map<ID,E> entitys;

    public MemoryRepo(Validator<E> validator) {
        this.validator = validator;
        this.entitys = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        if(entitys.containsKey(id))
            return Optional.of(entitys.get(id));
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        return entitys.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity cannot be null");
        validator.validate(entity);
        if(entitys.containsKey(entity.getId()))
            return Optional.of(entity);
        entitys.put(entity.getId(), entity);
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        if(entitys.containsKey(id))
            return Optional.of(entitys.remove(id));
        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity cannot be null");
        validator.validate(entity);
        if(entitys.containsKey(entity.getId())){
            entitys.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public int size() {
        return entitys.size();
    }

    public Validator<E> getValidator() {
        return validator;
    }
}
