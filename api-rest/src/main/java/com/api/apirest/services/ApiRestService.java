package com.api.apirest.services;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ApiRestModel;
import com.api.apirest.repositories.ApiRestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiRestService {

    //Injeção de depênndecia
    @Autowired
    ApiRestRepository apiRestRepository;

    @Autowired
    MessageProperty messageProperty;

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


    //Cripitografia de senha
//    public String passwordEncoder(String password) {
//        return securityConfiguration.passwordEncoder().encode(password);
//    }
}
