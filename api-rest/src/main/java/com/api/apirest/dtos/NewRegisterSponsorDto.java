package com.api.apirest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewRegisterSponsorDto {
    @NotBlank(message = "{error.name.notBlank}")
    @NotNull(message = "{error.name.notNull}")
    @Size(min = 2, max = 30, message = "{error.name.size}")
    private String name;

    @NotBlank(message = "{error.email.notBlank}")
    @NotNull(message = "{error.email.notNull}")
    @Size(max = 50, message = "{error.email.size}")
    @Email(message = "{error.email.invalid}")
    private String email;

    @NotBlank(message = "{error.password.notBlank}")
    @NotNull(message = "{error.password.notNull}")
    @Size(min = 5, max = 20, message = "{error.password.size}")
    private String password;
}