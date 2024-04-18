package com.web.springmvc.web_tin_tuc.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 5L;

    public UserAlreadyExistsException(String message) {
        super(message);

    }
}
