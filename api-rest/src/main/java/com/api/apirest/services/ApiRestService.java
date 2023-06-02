package com.api.apirest.services;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.dtos.RespLoginDto;
import com.api.apirest.dtos.loginDto;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.exceptions.handler.Unauthorized;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ApiRestModel;
import com.api.apirest.repositories.ApiRestRepository;
import com.api.apirest.security.SecurityConfigurations;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class ApiRestService {

    //Injeção de depênndecia
    @Autowired
    ApiRestRepository apiRestRepository;

    @Autowired
    MessageProperty messageProperty;

    @Autowired
    SecurityConfigurations securityConfigurations;

    @Transactional
    public ResponseEntity<Object> save (ApiRestModel apiRestModel) {
        //Verificar se usuário já existe
        if(apiRestRepository.existsByEmail(apiRestModel.getEmail())) {
            Messages messages = new Messages(messageProperty.getProperty("error.email.already.account"), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }

        ApiRestModel resp =  apiRestRepository.save(apiRestModel);
        Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @Transactional
    public ResponseEntity<Object> login(@NotNull loginDto loginDto) {
        if(!apiRestRepository.existsByEmail(loginDto.getEmail())) {
            throw new BadRequest(messageProperty.getProperty("error.account.notRegistered"));
        }

        ApiRestModel apiRestModel = apiRestRepository.findByEmail(loginDto.getEmail());

        if(BCrypt.checkpw(loginDto.getPassword(), apiRestModel.getPassword())) {
            Messages messages = new Messages(messageProperty.getProperty("ok.login.success"), HttpStatus.OK.value());

            RespLoginDto respLoginDto = new RespLoginDto(
                    apiRestModel.getExternalId(), apiRestModel.getEmail(), apiRestModel.getName()
            );

            return ResponseEntity.status(HttpStatus.OK).body(respLoginDto);
        }

        throw new Unauthorized(messageProperty.getProperty("error.unauthorized"));
    }


    //Cripitografia de senha
    public String passwordEncoder(String password) {
        return securityConfigurations.passwordEncoder().encode(password);
    }
}
