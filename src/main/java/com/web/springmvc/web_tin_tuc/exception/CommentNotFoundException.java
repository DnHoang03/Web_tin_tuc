package com.web.springmvc.web_tin_tuc.exception;

import java.io.Serial;

public class CommentNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 4L;

    public CommentNotFoundException(String message) {
        super(message);

    }
}
