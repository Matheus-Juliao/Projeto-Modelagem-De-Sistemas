package com.api.apirest.controllers;

import com.api.apirest.dtos.NewRegisterChildDto;
import com.api.apirest.dtos.NewRegisterSponsorDto;
import com.api.apirest.dtos.RespLoginDto;
import com.api.apirest.dtos.LoginDto;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
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
@SuppressWarnings("unused")
public class ApiController {

    @Autowired
    ApiRestService apiRestService;

    @PostMapping("/sponsor/new-register")
    @Operation(summary = "Register sponsor",  description = "Api for register a new sponsor on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createSponsor (@RequestBody @Valid NewRegisterSponsorDto newRegisterSponsorDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //Manipula os atributos do model
        SponsorModel sponsorModel = new SponsorModel();
        BeanUtils.copyProperties(newRegisterSponsorDto, sponsorModel);
        sponsorModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        sponsorModel.setExternalId(UUID.randomUUID().toString());
        sponsorModel.setPassword(apiRestService.passwordEncoder(sponsorModel.getPassword()));

        return apiRestService.saveSponsor(sponsorModel);
    }

    @PostMapping("/child/new-register")
    @Operation(summary = "Register child",  description = "Api for register an new child on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createChild (@RequestBody @Valid NewRegisterChildDto newRegisterChildDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //Manipula os atributos do model
        ChildModel childModel = new ChildModel();
        BeanUtils.copyProperties(newRegisterChildDto, childModel);
        childModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        childModel.setExternalId(UUID.randomUUID().toString());
        childModel.setPassword(apiRestService.passwordEncoder(childModel.getPassword()));

        return apiRestService.saveChild(childModel, newRegisterChildDto.getExternalIdSponsor());
    }

    @PostMapping("/login")
    @Operation(summary = "Logs the user into the application", description = "API for a user to authenticate on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespLoginDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto loginDto, @NotNull BindingResult result) throws BadRequest {
        if(result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        return apiRestService.login(loginDto);
    }

}
