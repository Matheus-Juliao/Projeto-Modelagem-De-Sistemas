package com.api.apirest.dtos;

import lombok.Data;

@Data
public class RespLoginDto {
    private  String externalId;
    private  String user;
    private  String name;

    public RespLoginDto(String externalId, String user, String name) {
        this.externalId = externalId;
        this.user = user;
        this.name = name;
    }
}
