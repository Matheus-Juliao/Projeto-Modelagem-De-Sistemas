package com.api.apirest.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "{error.user.notBlank}")
    @NotNull(message = "{error.user.notNull}")
    @Size(max = 50, message = "{error.user.size}")
    private String user;

    @NotBlank(message = "{error.password.notBlank}")
    @NotNull(message = "{error.password.notNull}")
    @Size(min = 5, max = 20, message = "{error.password.size}")
    private String password;

    private boolean isChild;
}
