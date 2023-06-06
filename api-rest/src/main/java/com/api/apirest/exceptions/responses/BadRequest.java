package com.api.apirest.exceptions.responses;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
public class BadRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Date timestamp;
    private final String message;
    private final String field;
    private final Integer code;

    public BadRequest(Date timestamp, String message, String field, Integer code) {
        this.timestamp = timestamp;
        this.message = message;
        this.field = field;
        this.code = code;
    }
}
