package com.api.apirest.services;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.dtos.RespChildDto;
import com.api.apirest.dtos.RespSponsorDto;
import com.api.apirest.dtos.LoginDto;
import com.api.apirest.dtos.SponsorDto;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    //Sponsor
    @Transactional
    public ResponseEntity<Object> saveSponsor (SponsorModel sponsorModel) {
        //Verificar se o responsável já está cadastrado no banco de dados
        if(sponsorRepository.existsByEmail(sponsorModel.getEmail())) {
            Messages messages = new Messages(messageProperty.getProperty("error.email.already.account"), HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }
        //Se não existir grava o responsável no banco de dados
        sponsorRepository.save(sponsorModel);
        Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    public ResponseEntity<Object> getSponsor(String externaId) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externaId);
        if(sponsorModel != null) {
            RespSponsorDto respSponsorDto = new RespSponsorDto(
                    sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
            );

            return ResponseEntity.status(HttpStatus.OK).body(respSponsorDto);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }

    public List<SponsorModel> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Transactional
    public  ResponseEntity<Object> deleteSponsor(String externalId) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalId);
        if(sponsorModel != null) {
            sponsorRepository.delete(sponsorModel);
            Messages messages = new Messages(messageProperty.getProperty("ok.user.delete"), HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }

    @Transactional
    public ResponseEntity<Object> updateSponsor(SponsorDto sponsorDto, String externalId) {
        //Verifica se não existe o externalId
        if(!sponsorRepository.existsByExternalId(externalId)) {
            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }

        //Faz o update
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalId);
        BeanUtils.copyProperties(sponsorDto, sponsorModel);
        sponsorModel.setPassword(passwordEncoder(sponsorModel.getPassword()));
        sponsorRepository.save(sponsorModel);
        RespSponsorDto respSponsorDto = new RespSponsorDto(
                sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
        );

        return ResponseEntity.status(HttpStatus.OK).body(respSponsorDto);
    }


    //Child
    @Transactional
    public ResponseEntity<Object> saveChild (ChildModel childModel, String externalIdSponsor) {
        try {
            //Verificar se a criança já está cadastrada no banco de dados
            if(childRepository.existsByNickname(childModel.getNickname())) {
                Messages messages = new Messages(messageProperty.getProperty("error.nickname.already.account"), HttpStatus.BAD_REQUEST.value());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
            }
            //Se não existir busca o responsável com externalId passado
            SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalIdSponsor);
            childModel.setUserCreator(externalIdSponsor);

            //verifica se a lista de child models foi inicializada
            if (sponsorModel.getChildModels() == null) {
                sponsorModel.setChildModels(new ArrayList<>());
            }
            //Grava na tabela auxiliar o id do responsável e o id da criança
            sponsorModel.getChildModels().add(childModel);
            childModel.getSponsorModels().add(sponsorModel);
            //Salva a criança no banco de dados
            childRepository.save(childModel);
            Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.OK).body(messages);
        } catch (NullPointerException e) {
            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.CREATED.value());
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
    }


    //Login
    @Transactional
    public ResponseEntity<Object> login(@NotNull LoginDto loginDto) {

        //Verificar se quem está entrando no sistema é uma criança
        if (loginDto.getIsChild()) {

            //Verificar se a criança não está cadastrada no sistema
            if(!childRepository.existsByNickname(loginDto.getUser())) {
                throw new BadRequest(messageProperty.getProperty("error.nickname.notRegistered"));
            }

            //Busca criança no banco de dados
            ChildModel childModel = childRepository.findByNickname(loginDto.getUser());

            //Verifica se a senha passada é a senha registrada no banco
            if(BCrypt.checkpw(loginDto.getPassword(), childModel.getPassword())) {
                RespChildDto respSponsorDto = new RespChildDto(
                        childModel.getExternalId(),
                        childModel.getName(),
                        childModel.getNickname(),
                        childModel.getAge()
                );
                return ResponseEntity.status(HttpStatus.OK).body(respSponsorDto);
            }
            throw new Unauthorized(messageProperty.getProperty("error.unauthorized"));
        }

        //Entrar no sistema como responsável
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        //Verifica se é o campo user veio preenchido e se é um e-mail
        if (StringUtils.hasLength(loginDto.getUser()) && loginDto.getUser().matches(regex)) {

            //Verificar se o responsável está cadastrado no sistema
            if(!sponsorRepository.existsByEmail(loginDto.getUser())) {
                throw new BadRequest(messageProperty.getProperty("error.email.notRegistered"));
            }

            //Busca responsável no banco de dados
            SponsorModel sponsorModel = sponsorRepository.findByEmail(loginDto.getUser());

            //Verifica se a senha passada é a senha registrada no banco
            if(BCrypt.checkpw(loginDto.getPassword(), sponsorModel.getPassword())) {
                RespSponsorDto respSponsorDto = new RespSponsorDto(
                        sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
                );

                return ResponseEntity.status(HttpStatus.OK).body(respSponsorDto);
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
