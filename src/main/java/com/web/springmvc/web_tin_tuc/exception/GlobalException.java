package com.web.springmvc.web_tin_tuc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ErrorObject> handleNewsNotFoundException(NewsNotFoundException ex, WebRequest request) {
        ErrorObject er = new ErrorObject();
        er.setStatusCode(HttpStatus.NOT_FOUND.value());
        er.setMessage(ex.getMessage());
        er.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorObject er = new ErrorObject();
        er.setStatusCode(HttpStatus.NOT_FOUND.value());
        er.setMessage(ex.getMessage());
        er.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorObject> handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest request) {
        ErrorObject er = new ErrorObject();
        er.setStatusCode(HttpStatus.NOT_FOUND.value());
        er.setMessage(ex.getMessage());
        er.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorObject> userAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ErrorObject er = new ErrorObject();
        er.setStatusCode(HttpStatus.CONFLICT.value());
        er.setMessage(ex.getMessage());
        er.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(er, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorObject> userAlreadyExistsException(CommentNotFoundException ex, WebRequest request) {
        ErrorObject er = new ErrorObject();
        er.setStatusCode(HttpStatus.NOT_FOUND.value());
        er.setMessage(ex.getMessage());
        er.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(er, HttpStatus.NOT_FOUND);
    }

}
