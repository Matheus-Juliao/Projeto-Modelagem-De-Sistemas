package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespSponsor {
    private String externalId;
    private String email;
    private String name;

    public RespSponsor(String externalId, String email, String name) {
        this.externalId = externalId;
        this.email = email;
        this.name = name;
    }
}
