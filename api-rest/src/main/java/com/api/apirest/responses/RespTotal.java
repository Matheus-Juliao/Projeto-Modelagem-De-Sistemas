package com.api.apirest.responses;

import lombok.Data;

@Data
public class RespTotal {
    private String externalId;
    private String externalIdSponsor;
    private String externalIdChild;
    private double total;
    private double totalValue;
    private double remainder;
    private String description;
    public RespTotal(String externalId, String externalIdSponsor, String externalIdChild, double total, double totalValue, double remainder, String description) {
        this.externalId = externalId;
        this.externalIdSponsor = externalIdSponsor;
        this.externalIdChild = externalIdChild;
        this.total = total;
        this.totalValue = totalValue;
        this.remainder = remainder;
        this.description = description;
    }

    public RespTotal(String externalId, String externalIdSponsor, String externalIdChild, double total, double remainder, String description) {
        this.externalId = externalId;
        this.externalIdSponsor = externalIdSponsor;
        this.externalIdChild = externalIdChild;
        this.total = total;
        this.remainder = remainder;
        this.description = description;
    }
}
