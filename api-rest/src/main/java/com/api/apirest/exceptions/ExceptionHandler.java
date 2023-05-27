package com.api.apirest.exceptions;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.exceptions.handler.NotFound;
import com.api.apirest.exceptions.handler.Unauthorized;
import com.api.apirest.exceptions.responses.BadRequest;
import com.api.apirest.exceptions.responses.NotFNotA;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestController
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionHandler {
    @Autowired
    MessageProperty messageProperty;

    @org.springframework.web.bind.annotation.ExceptionHandler(com.api.apirest.exceptions.handler.BadRequest.class)
    public final @NotNull ResponseEntity<BadRequest> handleBadResquestExceptions(Exception exception, WebRequest request) {

        String field = setField(exception);

        BadRequest badRequest =
                new BadRequest(
                        new Date(),
                        exception.getMessage(),
                        field,
                        HttpStatus.BAD_REQUEST.value()
                );


        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFound.class)
    public final @NotNull ResponseEntity<NotFNotA> handleNotFoundExceptions(Exception exception, WebRequest request) {

        String field = setField(exception);

        NotFNotA notFNotA =
                new NotFNotA(
                        new Date(),
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                );


        return new ResponseEntity<>(notFNotA, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Unauthorized.class)
    public final @NotNull ResponseEntity<NotFNotA> handleUnauthorizedExceptions(Exception exception, WebRequest request) {

        String field = setField(exception);

        NotFNotA notFNotA =
                new NotFNotA(
                        new Date(),
                        exception.getMessage(),
                        HttpStatus.UNAUTHORIZED.value()
                );


        return new ResponseEntity<>(notFNotA, HttpStatus.UNAUTHORIZED);
    }

    private @Nullable String setField(@NotNull Exception exception)  {

        if(exception.getMessage().compareTo(messageProperty.getProperty("error.name.notNull")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.name.notBlank")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.name.size")) == 0) {
            return "name";
        }

        if (exception.getMessage().compareTo(messageProperty.getProperty("error.email.notNull")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.email.notBlank")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.email.size")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.email.invalid")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.email.notRegistered")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.account.notRegistered")) == 0) {
            return "email";
        }

        if(exception.getMessage().compareTo(messageProperty.getProperty("error.password.notNull")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.password.notBlank")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.password.size")) == 0
                || exception.getMessage().compareTo(messageProperty.getProperty("error.password.incorrect")) == 0) {
            return "password";
        }

        return null;
    }

}
