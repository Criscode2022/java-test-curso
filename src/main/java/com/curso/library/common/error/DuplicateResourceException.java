package com.curso.library.common.error;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String field, String value) {
        super(field + " already exists: " + value);
    }
}
