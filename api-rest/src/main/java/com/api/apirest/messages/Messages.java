package com.api.apirest.messages;

import lombok.Getter;

@Getter
public class Messages {
    private final String message;
    private final Integer code;

    public Messages(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}