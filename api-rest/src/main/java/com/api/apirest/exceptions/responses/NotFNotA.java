package com.api.apirest.exceptions.responses;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
public class NotFNotA implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Date timestamp;
    private final String message;
    private final Integer code;

    public NotFNotA(Date timestamp, String message, Integer code) {
        this.timestamp = timestamp;
        this.message = message;
        this.code = code;
    }
}
