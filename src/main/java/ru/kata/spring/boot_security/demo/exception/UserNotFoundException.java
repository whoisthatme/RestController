package ru.kata.spring.boot_security.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class UserNotFoundException extends DataIntegrityViolationException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}