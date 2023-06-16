package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespChild {
    private String externalId;
    private String name;
    private String nickname;
    private int age;

    public RespChild(String externalId, String name, String nickname, int age) {
        this.externalId = externalId;
        this.name = name;
        this.nickname = nickname;
        this.age = age;
    }
}
