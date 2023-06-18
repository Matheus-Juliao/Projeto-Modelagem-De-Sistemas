package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespTotal {
    private String externalId;
    private String externalIdSponsor;
    private String externalIdChild;
    private double total;
    private String description;
    public RespTotal(String externalId, String externalIdSponsor, String externalIdChild, double total, String description) {
        this.externalId = externalId;
        this.externalIdSponsor = externalIdSponsor;
        this.externalIdChild = externalIdChild;
        this.total = total;
        this.description = description;
    }
}
