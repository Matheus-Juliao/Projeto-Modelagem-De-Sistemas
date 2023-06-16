package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespTask {
    private String externalId;
    private String name;
    private String description;
    private int weight;

    public RespTask(String externalId, String name, String description, int weight) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.weight = weight;
    }
}
