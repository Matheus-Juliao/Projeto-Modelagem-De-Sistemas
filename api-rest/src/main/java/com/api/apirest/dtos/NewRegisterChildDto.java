package com.api.apirest.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NewRegisterChildDto {

    @NotBlank(message = "{error.sponsorExternalId.notNull}")
    @NotNull(message = "{error.sponsorExternalId.notBlank=}")
    private String externalIdSponsor;

    @NotBlank(message = "{error.name.notBlank}")
    @NotNull(message = "{error.name.notNull}")
    @Size(min = 2, max = 30, message = "{error.name.size}")
    private String name;

    @NotBlank(message = "{error.nickname.notBlank=}")
    @NotNull(message = "{error.nickname.notNull=}")
    private String nickname;

    @NotNull(message = "{error.age.notNull}")
    @Min(value = 0, message = "{error.age.notBlank}")
    private int age;

    @NotBlank(message = "{error.password.notBlank}")
    @NotNull(message = "{error.password.notNull}")
    @Size(min = 5, max = 20, message = "{error.password.size}")
    private String password;
}
