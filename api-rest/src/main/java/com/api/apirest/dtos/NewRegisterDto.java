package com.api.apirest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewRegisterDto {
    @NotBlank(message = "{error.name.notBlank}")
    @NotNull(message = "{error.name.notNull}")
    @Size(min = 2, max = 30, message = "{error.name.size}")
    private String name;

    private String email;

    private String nickname;

    private int age;

    private boolean isChild;

    @NotBlank(message = "{error.password.notBlank}")
    @NotNull(message = "{error.password.notNull}")
    @Size(min = 5, max = 20, message = "{error.password.size}")
    private String password;

    private SponsorDto sponsor;
}