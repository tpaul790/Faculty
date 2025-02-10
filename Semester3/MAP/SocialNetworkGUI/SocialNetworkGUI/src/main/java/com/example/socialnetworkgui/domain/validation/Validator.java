package com.example.socialnetworkgui.domain.validation;

public interface Validator<E> {
    void validate(E e) throws ValidationException;
}
