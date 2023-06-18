package com.api.apirest.services;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.dtos.*;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.exceptions.handler.Unauthorized;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.*;
import com.api.apirest.repositories.*;
import com.api.apirest.responses.RespChild;
import com.api.apirest.responses.RespSponsor;
import com.api.apirest.responses.RespTask;
import com.api.apirest.responses.RespTotal;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiRestService {

    //Injeção de dependência
    @Autowired
    SponsorRepository sponsorRepository;

    @Autowired
    ChildRepository childRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TotalMonthlyAmountRepository totalRepository;

    @Autowired
    BonusRepository bonusRepository;

    @Autowired
    PenaltyRepository penaltyRepository;

    @Autowired
    MessageProperty messageProperty;

    @Autowired
    SecurityConfigurations securityConfigurations;

    //Sponsor
    //Já está no teste
    @Transactional
    public ResponseEntity<Object> createSponsor (SponsorModel sponsorModel) {
        //Verificar se o responsável já está cadastrado no banco de dados
        if(sponsorRepository.existsByEmail(sponsorModel.getEmail())) {
            Messages messages = new Messages(messageProperty.getProperty("error.email.already.account"), HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }
        //Se não existir grava o responsável no banco de dados
        sponsorRepository.save(sponsorModel);
        Messages messages = new Messages(messageProperty.getProperty("ok.user.registered"), HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(messages);
    }

    public ResponseEntity<Object> listSponsor(String externaId) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externaId);
        if(sponsorModel != null) {
            RespSponsor respSponsor = new RespSponsor(
                    sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
            );

            return ResponseEntity.status(HttpStatus.OK).body(respSponsor);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.sponsor.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
    }

    @Transactional
    public  ResponseEntity<Object> deleteSponsor(String externalId) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalId);
        if(sponsorModel != null) {
            sponsorRepository.delete(sponsorModel);
            Messages messages = new Messages(messageProperty.getProperty("ok.user.delete"), HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
    }

    @Transactional
    public ResponseEntity<Object> updateSponsor(SponsorDto sponsorDto, String externalId) {
        //Verifica se não existe o externalId
        if(!sponsorRepository.existsByExternalId(externalId)) {
            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }

        //Faz o update
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalId);
        BeanUtils.copyProperties(sponsorDto, sponsorModel);
        sponsorModel.setPassword(passwordEncoder(sponsorModel.getPassword()));
        sponsorRepository.save(sponsorModel);
        RespSponsor respSponsor = new RespSponsor(
                sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
        );

        return ResponseEntity.status(HttpStatus.OK).body(respSponsor);
    }


    //Child
   //Já está no teste
    @Transactional
    public ResponseEntity<Object> createChild (ChildModel childModel, String externalIdSponsor) {
        try {
            //Verificar se a criança já está cadastrada no banco de dados
            if(childRepository.existsByNickname(childModel.getNickname())) {
                Messages messages = new Messages(messageProperty.getProperty("error.nickname.already.account"), HttpStatus.BAD_REQUEST.value());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
            }
            //Se não existir busca o responsável com externalId passado
            SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalIdSponsor);

            if(sponsorModel != null) {
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
            }

            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);

        } catch (NullPointerException e) {
            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }
    }

    @Transactional
    public ResponseEntity<Object> updateChild(ChildDto childDto, String externalId) {
        //Verifica se não existe o externalId
        if(!childRepository.existsByExternalId(externalId)) {
            Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }

        //Faz o update
        ChildModel childModel = childRepository.findByExternalId(externalId);
        BeanUtils.copyProperties(childDto, childModel);
        childModel.setPassword(passwordEncoder(childModel.getPassword()));
        childRepository.save(childModel);
        RespChild respChild = new RespChild(
                childModel.getExternalId(), childModel.getName(), childModel.getNickname(), childModel.getAge()
        );

        return ResponseEntity.status(HttpStatus.OK).body(respChild);
    }

    public ResponseEntity<Object> listChild(String externaId) {
        List<ChildModel> childModels = childRepository.findByUserCreator(externaId);
        if(childModels != null) {
            List<RespChild> respChildList = new ArrayList<>();
            childModels.forEach(childModel -> {
                RespChild respChild = new RespChild(
                        childModel.getExternalId(),
                        childModel.getName(),
                        childModel.getNickname(),
                        childModel.getAge()
                );
                respChildList.add(respChild);
            });

            return ResponseEntity.status(HttpStatus.OK).body(respChildList);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.child.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
    }

    @Transactional
    public  ResponseEntity<Object> deleteChild(String externalId) {
        ChildModel childModel = childRepository.findByExternalId(externalId);
        if(childModel != null) {
            List<SponsorModel> sponsorModels = childModel.getSponsorModels();
            for (SponsorModel sponsorModel : sponsorModels) {
                sponsorModel.getChildModels().remove(childModel);
            }
            childModel.getSponsorModels().clear();
            childRepository.delete(childModel);
            Messages messages = new Messages(messageProperty.getProperty("ok.user.delete"), HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.externalIdSponsor.notFound"), HttpStatus.NOT_FOUND.value());

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
                RespChild respSponsorDto = new RespChild(
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
                RespSponsor respSponsor = new RespSponsor(
                        sponsorModel.getExternalId(), sponsorModel.getEmail(), sponsorModel.getName()
                );

                return ResponseEntity.status(HttpStatus.OK).body(respSponsor);
            }
            throw new Unauthorized(messageProperty.getProperty("error.unauthorized"));
        }
        throw new BadRequest(messageProperty.getProperty("error.email.invalid"));
    }


    //TotalMonthlyAmount
    @Transactional
    public ResponseEntity<Object> createTotal (TotalDto totalDto) {
        //Busca a criança com externalId passado
        ChildModel childModel = childRepository.findByExternalId(totalDto.getExternalIdChild());
        //Busca o responsável com externalId passado
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(totalDto.getExternalIdSponsor());

        //Verifica se existe Child
        if(childModel == null) {
            Messages messages = new Messages(messageProperty.getProperty("error.child.notFound"), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }
        //Verifica se existe Sponsor
        if(sponsorModel == null) {
            Messages messages = new Messages(messageProperty.getProperty("error.sponsor.notFound"), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }
        //Verifica se já tem adicionado algum valor total para essa criança
        if(totalRepository.existsByTotal(childModel.getIdChild())) {
            Messages messages = new Messages(messageProperty.getProperty("error.total.unique"), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }
        //Manipula os atributos do model
        TotalMonthlyAmountModel totalModel = new TotalMonthlyAmountModel();
        BeanUtils.copyProperties(totalDto, totalModel);
        totalModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        totalModel.setExternalId(UUID.randomUUID().toString());

        //Adiciona a criança no novo total mensal que está criando
        childModel.addTotal(totalModel);
        //Adiciona o responsável no novo total mensal que está criando
        sponsorModel.addTotal(totalModel);
        //Salva o total mensal no banco de dados
        totalRepository.save(totalModel);

        RespTotal respTotal = new RespTotal(
                totalModel.getExternalId(),
                sponsorModel.getExternalId(),
                childModel.getExternalId(),
                totalModel.getTotal(),
                totalModel.getDescription()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respTotal);
    }

    @Transactional
    public ResponseEntity<Object> showTotal(String externaId) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(externaId);
        if(sponsorModel != null) {
            TotalMonthlyAmountModel totalModelSponsor = totalRepository.findBySponsorModel(sponsorModel);
            if(totalModelSponsor != null) {
                Optional<ChildModel> childModelSponsor = childRepository.findById(totalModelSponsor.getChildModel().getIdChild());
                RespTotal respTotal = new RespTotal(
                        totalModelSponsor.getExternalId(),
                        externaId,
                        childModelSponsor.get().getExternalId(),
                        totalModelSponsor.getTotal(),
                        totalModelSponsor.getDescription()
                );
                return ResponseEntity.status(HttpStatus.OK).body(respTotal);
            }
        }

        ChildModel childModel = childRepository.findByExternalId(externaId);
        if(childModel != null) {
            TotalMonthlyAmountModel totalModelChild = totalRepository.findByChildModel(childModel);
            if(totalModelChild != null) {
                Optional<ChildModel> childModelSponsor = childRepository.findById(totalModelChild.getChildModel().getIdChild());
                RespTotal respTotal = new RespTotal(
                        totalModelChild.getExternalId(),
                        externaId,
                        childModelSponsor.get().getExternalId(),
                        totalModelChild.getTotal(),
                        totalModelChild.getDescription()
                );
                return ResponseEntity.status(HttpStatus.OK).body(respTotal);
            }
        }
        Messages messages = new Messages(messageProperty.getProperty("error.total.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
    }

    @Transactional
    public  ResponseEntity<Object> deleteTotal(String externalId) {
        TotalMonthlyAmountModel totalModel = totalRepository.findByExternalId(externalId);
        //Remover
        if(totalModel != null) {
            totalModel.getSponsorModel().removeTotal(totalModel);
            totalModel.getChildModel().removeTotal(totalModel);
            totalRepository.delete(totalModel);
            Messages messages = new Messages(messageProperty.getProperty("ok.total.delete"), HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.total.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }


    //Bonus
    @Transactional
    public ResponseEntity<Object> applyBonus(BonusDto bonusDto) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(bonusDto.getExternalIdSponsor());
        if(sponsorModel != null) {
            TotalMonthlyAmountModel totaModel = totalRepository.findByExternalId(bonusDto.getExternalIdTotal());
            if(totaModel != null) {
                Optional<ChildModel> childModelSponsor = childRepository.findById(totaModel.getChildModel().getIdChild());
                //Manipula os atributos do model
                BonusModel bonusModel = new BonusModel();
                BeanUtils.copyProperties(bonusDto, bonusModel);
                bonusModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
                bonusModel.setExternalId(UUID.randomUUID().toString());

                //Adiciona o responsável no novo bônus mensal que está criando
                sponsorModel.addBonus(bonusModel);

                //Salva o bônus no banco de dados
                bonusRepository.save(bonusModel);

                //Atualiza o valor no total
                BigDecimal result = BigDecimal.valueOf(totaModel.getTotal()).add(BigDecimal.valueOf(bonusModel.getBonus()));
                result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
                totaModel.setTotal(result.doubleValue());
                totalRepository.save(totaModel);

                RespTotal respTotal = new RespTotal(
                        totaModel.getExternalId(),
                        sponsorModel.getExternalId(),
                        childModelSponsor.get().getExternalId(),
                        result.doubleValue(),
                        totaModel.getDescription()
                );

                return ResponseEntity.status(HttpStatus.CREATED).body(respTotal);
            }
            Messages messages = new Messages(messageProperty.getProperty("error.total.notFound"), HttpStatus.NOT_FOUND.value());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.sponsor.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }


    //Penalties
    @Transactional
    public ResponseEntity<Object> applyPenalty(PenaltyDto penaltyDto) {
        SponsorModel sponsorModel = sponsorRepository.findByExternalId(penaltyDto.getExternalIdSponsor());
        if(sponsorModel != null) {
            TotalMonthlyAmountModel totaModel = totalRepository.findByExternalId(penaltyDto.getExternalIdTotal());
            if(totaModel != null) {
                Optional<ChildModel> childModelSponsor = childRepository.findById(totaModel.getChildModel().getIdChild());
                //Manipula os atributos do model
                PenaltyModel penaltyModel = new PenaltyModel();
                BeanUtils.copyProperties(penaltyDto, penaltyModel);
                penaltyModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
                penaltyModel.setExternalId(UUID.randomUUID().toString());

                //Adiciona o responsável na nova penalidade mensal que está criando
                sponsorModel.addPenalty(penaltyModel);

                //Salva a penalidade no banco de dados
                penaltyRepository.save(penaltyModel);

                //Atualiza o valor no total
                double result = totaModel.getTotal() - penaltyModel.getPenalty();
                totaModel.setTotal(result);
                totalRepository.save(totaModel);

                RespTotal respTotal = new RespTotal(
                        totaModel.getExternalId(),
                        sponsorModel.getExternalId(),
                        childModelSponsor.get().getExternalId(),
                        result,
                        totaModel.getDescription()
                );

                return ResponseEntity.status(HttpStatus.CREATED).body(respTotal);
            }
            Messages messages = new Messages(messageProperty.getProperty("error.total.notFound"), HttpStatus.NOT_FOUND.value());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
        }
        Messages messages = new Messages(messageProperty.getProperty("error.sponsor.notFound"), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }


    //Task
    @Transactional
    public ResponseEntity<Object> createTask (TaskModel taskModel, String externalIdSponsor) {
        try {
            //Busca o responsável com externalId passado
            SponsorModel sponsorModel = sponsorRepository.findByExternalId(externalIdSponsor);
            //Adiciona a nova tarefa ao Sponsor que está criando
            sponsorModel.addTask(taskModel);
            //Salva a criança no banco de dados
            taskRepository.save(taskModel);
            RespTask respTask = new RespTask(
                    taskModel.getExternalId(), taskModel.getName(), taskModel.getDescription(), taskModel.getWeight()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(respTask);
        } catch (NullPointerException e) {
            Messages messages = new Messages(messageProperty.getProperty("error.sponsor.notFound"), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messages);
        }
    }


    //Criptografia de senha
    public String passwordEncoder(String password) {
        return securityConfigurations.passwordEncoder().encode(password);
    }
}