package com.api.apirest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TotalDto {
    @NotBlank(message = "{error.externalIdSponsor.notBlank}")
    @NotNull(message = "{error.externalIdSponsor.notNull}")
    private String externalIdSponsor;

    @NotBlank(message = "{error.externalIdChild.notBlank}")
    @NotNull(message = "{error.externalIdChild.notNull}")
    private String externalIdChild;

    @NotNull(message = "{error.total.notNull}")
    @Min(value = 0, message = "{error.total.notBlank}")
    private double total;

    @NotBlank(message = "{error.description.notBlank}")
    @NotNull(message = "{error.description.notNull}")
    @Size(message = "{error.description.size}", max = 300)
    private String description;
}
