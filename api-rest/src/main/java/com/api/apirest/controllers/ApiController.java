package com.api.apirest.controllers;

import com.api.apirest.dtos.*;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
import com.api.apirest.models.TaskModel;
import com.api.apirest.responses.RespSponsor;
import com.api.apirest.responses.RespTask;
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

        return apiRestService.createSponsor(sponsorModel);
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
            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespSponsor.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> listSponsor(@PathVariable String externalId) {
        return apiRestService.listSponsor(externalId);
    }

    @DeleteMapping("delete-sponsor/{externalId}")
    @Operation(summary = "Delete a sponsor", description = "API to delete a sponsor on the platform")
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

        return apiRestService.createChild(childModel, childDto.getExternalIdSponsor());
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
    @Operation(summary = "Delete a child", description = "API to delete a child on the platform")
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
            @ApiResponse(responseCode = "201", description = "Created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespSponsor.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto loginDto, @NotNull BindingResult result) throws BadRequest {
        if(result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return apiRestService.login(loginDto);
    }


    //Task
    @PostMapping("task/new-task")
    @Operation(summary = "Register new task",  description = "Api for register an new task on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = RespTask.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createTask (@RequestBody @Valid TaskDto taskDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        //Manipula os atributos do model
        TaskModel taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        taskModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setExternalId(UUID.randomUUID().toString());

        return apiRestService.createTask(taskModel, taskDto.getExternalIdSponsor());
    }

//    @PutMapping("/update-task/{externalId}")
//    @Operation(summary = "Update task",  description = "Api for update a task on the platform")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RespTask.class)) }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
//    })
//    public ResponseEntity<Object> updateTask(@PathVariable String externalId, @RequestBody @Valid ChildDto childDto, @NotNull BindingResult result) throws BadRequest {
//        if (result.hasErrors()) {
//            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
//        }
//        return apiRestService.updateChild(childDto, externalId);
//    }


    //TotalMonthlyAmount
    @PostMapping("/total-monthly-amount/new-total")
    @Operation(summary = "Register TotalMonthlyAmount",  description = "Api for register a new TotalMonthlyAmount on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =  { @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> createTotal (@RequestBody @Valid TotalDto totalDto, BindingResult result) throws BadRequest {
        if (result.hasErrors()) {
            throw new BadRequest(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        return apiRestService.createTotal(totalDto);
    }

    @DeleteMapping("delete-total-monthly-amount/{externalId}")
    @Operation(summary = "Delete a total-monthly-amount", description = "API to delete a total-monthly-amount on the platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Object> deleteTotal(@PathVariable String externalId){
        return apiRestService.deleteTotal(externalId);
    }
}