package com.api.apirest.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequest(String exception) {
        super(exception);
    }


}
