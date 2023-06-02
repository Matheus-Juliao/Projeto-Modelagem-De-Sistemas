package com.api.apirest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RespLoginDto {
    private  String externalId;
    private  String email;
    private  String name;

    public RespLoginDto () {}

    public RespLoginDto(String externalId, String email, String name) {
        this.externalId = externalId;
        this.email = email;
        this.name = name;
    }
}
