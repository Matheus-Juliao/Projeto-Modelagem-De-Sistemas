package com.api.apirest.dtos;

import lombok.Data;

@Data
public class RespChildDto {
    private String externalId;
    private String name;
    private String nickname;
    private int age;

    public RespChildDto(String externalId, String name, String nickname, int age) {
        this.externalId = externalId;
        this.name = name;
        this.nickname = nickname;
        this.age = age;
    }
}
