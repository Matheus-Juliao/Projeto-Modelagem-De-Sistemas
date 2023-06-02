package com.api.apirest.controllers;

import com.api.apirest.dtos.ApiRestDto;
import com.api.apirest.dtos.loginDto;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ApiRestModel;
import com.api.apirest.services.ApiRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@RestController
//Permitir acesso de qualquer fonte
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("/api-rest")
public class ApiController {

    @Autowired
    ApiRestService apiRestService;

    @PostMapping("/new-register")
    @Operation(summary = "Register user",  description = "Api for register a new user on the plataform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createUser (@RequestBody @Valid ApiRestDto apiRestDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        ApiRestModel apiRestModel = new ApiRestModel();

        BeanUtils.copyProperties(apiRestDto, apiRestModel);
        apiRestModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        apiRestModel.setExternalId(UUID.randomUUID().toString());
        apiRestModel.setPassword(apiRestService.passwordEncoder(apiRestModel.getPassword()));

        return apiRestService.save(apiRestModel);
    }

    @PostMapping("/login")
    @Operation(summary = "Logs the user into the application", description = "API for a user to authenticate on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> login(@RequestBody @Valid loginDto loginDto, @NotNull BindingResult result) throws BadRequest {
        if(result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        return apiRestService.login(loginDto);
    }

}
