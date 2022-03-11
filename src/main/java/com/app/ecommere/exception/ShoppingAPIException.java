package com.app.ecommere.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ShoppingAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public ShoppingAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
