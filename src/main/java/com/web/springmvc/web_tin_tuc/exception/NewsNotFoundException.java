package com.web.springmvc.web_tin_tuc.exception;

import java.io.Serial;

public class NewsNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public NewsNotFoundException(String message) {
        super(message);

    }
}
