package com.api.apirest.dtos;

import lombok.Data;

@Data
public class RespSponsorDto {
    private String externalId;
    private String email;
    private String name;

    public RespSponsorDto(String externalId, String email, String name) {
        this.externalId = externalId;
        this.email = email;
        this.name = name;
    }
}
