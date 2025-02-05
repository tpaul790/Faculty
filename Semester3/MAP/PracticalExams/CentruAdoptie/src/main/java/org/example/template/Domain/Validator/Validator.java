package org.example.template.Domain.Validator;

public interface Validator<E> {
    boolean validate(E e);
}
