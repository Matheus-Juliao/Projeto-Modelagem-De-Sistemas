package com.api.apirest.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class Unauthorized extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public Unauthorized(String exception) {
        super(exception);
    }

}
