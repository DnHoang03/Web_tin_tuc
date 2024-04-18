package com.web.springmvc.web_tin_tuc.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2L;

    public UserNotFoundException(String message) {
        super(message);

    }
}
