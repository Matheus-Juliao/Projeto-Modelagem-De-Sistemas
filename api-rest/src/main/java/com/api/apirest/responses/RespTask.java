package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespTask {
    private String externalId;
    private String externalIdSponsor;
    private String externalIdChild;
    private String externalIdTotal;
    private String name;
    private String description;
    private int weight;
    private double value;
    private boolean isComplete;
    private double remainder;

    public RespTask(String externalId, String externalIdSponsor, String externalIdChild, String externalIdTotal, String name, String description, int weight, double value, boolean isComplete, double remainder) {
        this.externalId = externalId;
        this.externalIdSponsor = externalIdSponsor;
        this.externalIdChild = externalIdChild;
        this.externalIdTotal = externalIdTotal;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = value;
        this.isComplete = isComplete;
        this.remainder = remainder;
    }
}