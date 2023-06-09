package com.api.apirest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskDto {
    @NotBlank(message = "{error.externalIdSponsor.notBlank}")
    @NotNull(message = "{error.externalIdSponsor.notNull}")
    private String externalIdSponsor;

    @NotBlank(message = "{error.name.notBlank}")
    @NotNull(message = "{error.name.notNull}")
    @Size(min = 2, max = 50, message = "{error.nameTask.size}")
    private String name;

    @NotBlank(message = "{error.description.notBlank}")
    @NotNull(message = "{error.description.notNull}")
    @Size(message = "{error.description.size}", max = 300)
    private String description;

    @NotNull(message = "{error.weight.notNull}")
    @Min(value = 1, message = "{error.weight.notBlank}")
    private int weight;
}