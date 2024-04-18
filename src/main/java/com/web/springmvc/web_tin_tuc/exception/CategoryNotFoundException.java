package com.web.springmvc.web_tin_tuc.exception;

import java.io.Serial;

public class CategoryNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3L;

    public CategoryNotFoundException(String message) {
        super(message);

    }
}
