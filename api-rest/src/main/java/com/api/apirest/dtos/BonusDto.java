package com.api.apirest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BonusDto {
    @NotBlank(message = "{error.externalIdSponsor.notBlank}")
    @NotNull(message = "{error.externalIdSponsor.notNull}")
    private String externalIdSponsor;

    @NotBlank(message = "{error.externalIdTotal.notBlank}")
    @NotNull(message = "{error.externalIdTotal.notNull}")
    private String externalIdTotal;

    @NotNull(message = "{error.bonus.notNull}")
    @Min(value = 0, message = "{error.bonus.notBlank}")
    private double bonus;
}