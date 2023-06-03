package com.api.apirest.services;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.dtos.RespLoginDto;
import com.api.apirest.dtos.LoginDto;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.exceptions.handler.Unauthorized;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
import com.api.apirest.repositories.ChildRepository;
import com.api.apirest.repositories.SponsorRepository;
import com.api.apirest.security.SecurityConfigurations;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ApiRestService {

    //Injeção de dependência
    @Autowired
    SponsorRepository sponsorRepository;

    @Autowired
    ChildRepository childRepository;

    @Autowired
    MessageProperty messageProperty;

    @Autowired
    SecurityConfigurations securityConfigurations;

    @Transactional
    public ResponseEntity<Object> saveSponsor (SponsorModel sponsorModel) {

        //Verificar se usuário já está cadastrado no banco de dados
        if(sponsorRepository.existsByEmail(sponsorModel.getEmail())) {
            Messages messages = new Messages(messageProperty.getProperty("error.email.already.account"), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }

        //Se não existir grava o usuário no banco de dados
        sponsorRepository.save(sponsorModel);
        Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @Transactional
    public ResponseEntity<Object> saveChild (ChildModel childModel) {

        //Verificar se usuário já está cadastrado no banco de dados
        if(sponsorRepository.existsByEmail(childModel.getNickname())) {
            Messages messages = new Messages(messageProperty.getProperty("error.email.already.account"), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }

        //Se não existir grava o usuário no banco de dados
        childRepository.save(childModel);
        Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @Transactional
    public ResponseEntity<Object> login(@NotNull LoginDto loginDto) {

        //Verificar se quem está entrando no sistema é uma criança
        if (loginDto.isChild()) {

            //Verificar se a criança está cadastrada no sistema
            if(childRepository.existsByNickname(loginDto.getUser())) {
                throw new BadRequest(messageProperty.getProperty("error.account.notRegistered"));
            }

            //Busca criança no banco de dados
            ChildModel childModel = childRepository.findByNickname(loginDto.getUser());

            //Verifica se a senha passada é a senha registrada no banco
            if(BCrypt.checkpw(loginDto.getPassword(), childModel.getPassword())) {
                RespLoginDto respLoginDto = new RespLoginDto(
                        childModel.getExternalId(), childModel.getNickname(), childModel.getName()
                );

                return ResponseEntity.status(HttpStatus.OK).body(respLoginDto);
            }

            throw new Unauthorized(messageProperty.getProperty("error.unauthorized"));
        }

        //Entrar no sistema como responsável
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        //Verifica se é o campo user veio preenchido e se é um e-mail
        if (StringUtils.hasLength(loginDto.getUser()) && loginDto.getUser().matches(regex)) {

            //Verificar se o responsável está cadastrado no sistema
            if(!sponsorRepository.existsByEmail(loginDto.getUser())) {
                throw new BadRequest(messageProperty.getProperty("error.account.notRegistered"));
            }

            //Busca responsável no banco de dados
            SponsorModel sponsorModel = sponsorRepository.findByEmail(loginDto.getUser());

            //Verifica se a senha passada é a senha registrada no banco
            if(BCrypt.checkpw(loginDto.getPassword(), sponsorModel.getPassword())) {
                RespLoginDto respLoginDto = new RespLoginDto(
                        sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
                );

                return ResponseEntity.status(HttpStatus.OK).body(respLoginDto);
            }

            throw new Unauthorized(messageProperty.getProperty("error.unauthorized"));
        }

        throw new BadRequest(messageProperty.getProperty("error.email.invalid"));
    }


    //Criptografia de senha
    public String passwordEncoder(String password) {
        return securityConfigurations.passwordEncoder().encode(password);
    }
}
