package domain.validator;

public abstract class Validator<E> {
    public abstract String validate(E entity);
}
