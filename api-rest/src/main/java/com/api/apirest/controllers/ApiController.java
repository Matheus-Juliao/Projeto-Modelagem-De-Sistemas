package com.api.apirest.controllers;

import com.api.apirest.dtos.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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

    //Sponsor
    @PostMapping("/sponsor/new-register")
    @Operation(summary = "Register sponsor",  description = "Api for register a new sponsor on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createSponsor (@RequestBody @Valid SponsorDto sponsorDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //Manipula os atributos do model
        SponsorModel sponsorModel = new SponsorModel();
        BeanUtils.copyProperties(sponsorDto, sponsorModel);
        sponsorModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        sponsorModel.setExternalId(UUID.randomUUID().toString());
        sponsorModel.setPassword(apiRestService.passwordEncoder(sponsorModel.getPassword()));

        return apiRestService.saveSponsor(sponsorModel);
    }

    @PutMapping("/update-sponsor/{externalId}")
    @Operation(summary = "Update sponsor",  description = "Api for update a sponsor on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SponsorDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> updateSponsor(@PathVariable String externalId, @RequestBody @Valid SponsorDto sponsorDto, @NotNull BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return apiRestService.updateSponsor(sponsorDto, externalId);
    }

    @GetMapping("list-sponsor/{externalId}")
    @Operation(summary = "Returns sponsor", description = "API to fetch a sponsor on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespSponsorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> listSponsor(@PathVariable String externalId) {
        return apiRestService.listSponsor(externalId);
    }

    @DeleteMapping("delete-sponsor/{externalId}")
    @Operation(summary = "Delete a sponsor's account", description = "API to delete a sponsor account on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> deleteSponsor(@PathVariable String externalId){
        return apiRestService.deleteSponsor(externalId);
    }


    //Child
    @PostMapping("/child/new-register")
    @Operation(summary = "Register child",  description = "Api for register an new child on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createChild (@RequestBody @Valid ChildDto childDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //Manipula os atributos do model
        ChildModel childModel = new ChildModel();
        BeanUtils.copyProperties(childDto, childModel);
        childModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        childModel.setExternalId(UUID.randomUUID().toString());
        childModel.setPassword(apiRestService.passwordEncoder(childModel.getPassword()));

        return apiRestService.saveChild(childModel, childDto.getExternalIdSponsor());
    }

    @PutMapping("/update-child/{externalId}")
    @Operation(summary = "Update child",  description = "Api for update a child on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ChildDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> updateChild(@PathVariable String externalId, @RequestBody @Valid ChildDto childDto, @NotNull BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return apiRestService.updateChild(childDto, externalId);
    }

    @GetMapping("/listAllChild/{externalId}")
    @Operation(summary = "Returns all child of Sponsor", description = "API to list all child of Sponsor and their information on the platform")
    public ResponseEntity<Object> listChild(@PathVariable String externalId) {
        return apiRestService.listChild(externalId);
    }

    @DeleteMapping("delete-child/{externalId}")
    @Operation(summary = "Delete a child's account", description = "API to delete a child account on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> deleteChild(@PathVariable String externalId){
        return apiRestService.deleteChild(externalId);
    }


    //Login
    @PostMapping("/login")
    @Operation(summary = "Logs the user into the application", description = "API for a user to authenticate on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespSponsorDto.class)) }),
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
