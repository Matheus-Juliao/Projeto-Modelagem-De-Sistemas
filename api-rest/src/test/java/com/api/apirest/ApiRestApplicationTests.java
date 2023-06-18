package com.api.apirest;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.dtos.ChildDto;
import com.api.apirest.dtos.LoginDto;
import com.api.apirest.dtos.SponsorDto;
import com.api.apirest.exceptions.handler.BadRequest;
import com.api.apirest.exceptions.handler.Unauthorized;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
import com.api.apirest.repositories.ChildRepository;
import com.api.apirest.repositories.SponsorRepository;
import com.api.apirest.responses.RespChild;
import com.api.apirest.responses.RespSponsor;
import com.api.apirest.security.SecurityConfigurations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.api.apirest.services.ApiRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApiRestApplicationTests {
    @Mock
    private ChildRepository childRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Mock
    private SponsorRepository sponsorRepository;
    @Mock
    private SecurityConfigurations securityConfigurations;

    @Mock
    private MessageProperty messageProperty;

    @InjectMocks
    private ApiRestService apiRestService;

    @BeforeEach
    public void setup() {
        when(messageProperty.getProperty("error.nickname.already.account")).thenReturn("Nickname already exists");
        when(messageProperty.getProperty("ok.user.registered")).thenReturn("User registered");
        when(messageProperty.getProperty("error.externalIdSponsor.notFound")).thenReturn("Sponsor not found");
    }
    @Test
    void testCreateSponsor_WhenSponsorDoesNotExist_ExpectCreated() {
        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByEmail(anyString())).thenReturn(false);
        when(sponsorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("error.email.already.account")).thenReturn("Error: Email already exists.");
        when(messageProperty.getProperty("ok.user.registered")).thenReturn("User registered");

        // Criação do objeto SponsorModel de exemplo
        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setEmail("test@example.com");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.createSponsor(sponsorModel);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals("User registered", messages.getMessage());

        // Verifica se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(sponsorModel.getEmail());
        // Verifica se o método save() foi chamado corretamente
        verify(sponsorRepository, times(1)).save(sponsorModel);
    }



    @Test
    void testCreateSponsor_WhenSponsorExists_ExpectBadRequest() {
        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByEmail(anyString())).thenReturn(true);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("error.email.already.account")).thenReturn("Error: Email already exists.");

        // Criação do objeto SponsorModel de exemplo
        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setEmail("test@example.com");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.createSponsor(sponsorModel);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals("Error: Email already exists.", messages.getMessage());

        // Verifica se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(sponsorModel.getEmail());
        // Verifica se o método save() não foi chamado
        verify(sponsorRepository, never()).save(any());
    }


    @Test
    public void testCreateChild_WhenChildExists_ExpectBadRequest() {
        ChildModel childModel = new ChildModel();
        childModel.setNickname("test_nickname");

        when(childRepository.existsByNickname(childModel.getNickname())).thenReturn(true);

        ResponseEntity<Object> response = apiRestService.createChild(childModel, "test_external_id");

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertEquals("Nickname already exists", messages.getMessage());
        //assertEquals(HttpStatus.BAD_REQUEST.value(), messages.getMessage());
    }

    @Test
    void testListSponsor_WhenSponsorExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";
        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setExternalId(externalId);
        sponsorModel.setEmail("test@example.com");
        sponsorModel.setName("Test Sponsor");

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.findByExternalId(externalId)).thenReturn(sponsorModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.listSponsor(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespSponsor);
        RespSponsor respSponsor = (RespSponsor) response.getBody();
        assertEquals(externalId, respSponsor.getExternalId());
        assertEquals("test@example.com", respSponsor.getEmail());
        assertEquals("Test Sponsor", respSponsor.getName());

        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método getProperty() não foi chamado
        verify(messageProperty, never()).getProperty(anyString());
    }

    @Test
    void testUpdateSponsor_WhenSponsorExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";
        SponsorDto sponsorDto = new SponsorDto();
        sponsorDto.setEmail("updated_email@example.com");
        sponsorDto.setName("Updated Sponsor");
        sponsorDto.setPassword("password123"); // Defina uma senha válida aqui

        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setExternalId(externalId);
        sponsorModel.setEmail("original_email@example.com");
        sponsorModel.setName("Original Sponsor");
        sponsorModel.setPassword("original_password"); // Defina uma senha válida aqui

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByExternalId(externalId)).thenReturn(true);
        when(sponsorRepository.findByExternalId(externalId)).thenReturn(sponsorModel);
        when(sponsorRepository.save(any(SponsorModel.class))).thenReturn(sponsorModel);

        // Configuração do comportamento do mock do SecurityConfigurations
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        when(securityConfigurations.passwordEncoder()).thenReturn(passwordEncoder);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.updateSponsor(sponsorDto, externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespSponsor);
        RespSponsor respSponsor = (RespSponsor) response.getBody();
        assertEquals(externalId, respSponsor.getExternalId());
        assertEquals("updated_email@example.com", respSponsor.getEmail());
        assertEquals("Updated Sponsor", respSponsor.getName());

        // Verifica se o método existsByExternalId() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByExternalId(externalId);
        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método save() foi chamado corretamente
        verify(sponsorRepository, times(1)).save(any(SponsorModel.class));
        // Verifica se o método getProperty() não foi chamado
        verify(messageProperty, never()).getProperty(anyString());
    }

    @Test
    void testDeleteSponsor_WhenSponsorExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";

        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setExternalId(externalId);

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.findByExternalId(externalId)).thenReturn(sponsorModel);
        doNothing().when(sponsorRepository).delete(sponsorModel);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("ok.user.delete")).thenReturn("User deleted successfully");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.deleteSponsor(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals(HttpStatus.OK.value(), messages.getCode());
        assertEquals("User deleted successfully", messages.getMessage());

        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método delete() foi chamado corretamente
        verify(sponsorRepository, times(1)).delete(sponsorModel);
        // Verifica se o método getProperty() foi chamado corretamente
        verify(messageProperty, times(1)).getProperty("ok.user.delete");
    }
    @Test
    void testDeleteSponsor_WhenSponsorDoesNotExist_ExpectNotFoundResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.findByExternalId(externalId)).thenReturn(null);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty(anyString())).thenReturn("Not Found");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.deleteSponsor(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), messages.getCode());
        assertEquals("Not Found", messages.getMessage());

        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método delete() não foi chamado
        verify(sponsorRepository, never()).delete(any(SponsorModel.class));
        // Verifica se o método getProperty() foi chamado corretamente
        verify(messageProperty, times(1)).getProperty(anyString());
    }
    @Test
    public void testCreateChild_WhenSponsorNotFound_ExpectNotFound() {
        ChildModel childModel = new ChildModel();
        childModel.setNickname("test_nickname");

        when(childRepository.existsByNickname(childModel.getNickname())).thenReturn(false);
        when(sponsorRepository.findByExternalId("test_external_id")).thenReturn(null);

        ResponseEntity<Object> response = apiRestService.createChild(childModel, "test_external_id");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertEquals("Sponsor not found", messages.getMessage());
        //assertEquals(HttpStatus.NOT_FOUND.value(), messages.getMessage());
    }

    @Test
    public void testCreateChild_WhenChildCreated_ExpectCreated() {
        ChildModel childModel = new ChildModel();
        childModel.setNickname("test_nickname");

        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setExternalId("test_external_id");
        sponsorModel.setChildModels(new ArrayList<>());

        when(childRepository.existsByNickname(childModel.getNickname())).thenReturn(false);
        when(sponsorRepository.findByExternalId("test_external_id")).thenReturn(sponsorModel);
        when(childRepository.save(childModel)).thenReturn(childModel);

        ResponseEntity<Object> response = apiRestService.createChild(childModel, "test_external_id");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertEquals("User registered", messages.getMessage());
        //assertEquals(HttpStatus.CREATED.value(), messages.getMessage());
    }

    @Test
    void testListChild_WhenChildExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";
        ChildModel childModel1 = new ChildModel();
        childModel1.setExternalId("child1_external_id");
        childModel1.setName("Child 1");
        childModel1.setNickname("child1");
        childModel1.setAge(5);

        ChildModel childModel2 = new ChildModel();
        childModel2.setExternalId("child2_external_id");
        childModel2.setName("Child 2");
        childModel2.setNickname("child2");
        childModel2.setAge(7);

        List<ChildModel> childModels = Arrays.asList(childModel1, childModel2);

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.findByUserCreator(externalId)).thenReturn(childModels);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.listChild(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        List<RespChild> respChildList = (List<RespChild>) response.getBody();
        assertEquals(2, respChildList.size());

        RespChild respChild1 = respChildList.get(0);
        assertEquals("child1_external_id", respChild1.getExternalId());
        assertEquals("Child 1", respChild1.getName());
        assertEquals("child1", respChild1.getNickname());
        assertEquals(5, respChild1.getAge());

        RespChild respChild2 = respChildList.get(1);
        assertEquals("child2_external_id", respChild2.getExternalId());
        assertEquals("Child 2", respChild2.getName());
        assertEquals("child2", respChild2.getNickname());
        assertEquals(7, respChild2.getAge());

        // Verifica se o método findByUserCreator() foi chamado corretamente
        verify(childRepository, times(1)).findByUserCreator(externalId);
        // Verifica se o método getProperty() não foi chamado
        verify(messageProperty, never()).getProperty(anyString());
    }

    @Test
    void testListChild_WhenChildDoesNotExist_ExpectNotFoundResponse() {
        // Criação dos dados de exemplo
        String externalId = "non_existing_external_id";

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.findByUserCreator(externalId)).thenReturn(null);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("error.child.notFound")).thenReturn("Child not found");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.listChild(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals("Child not found", messages.getMessage());

        // Verifica se o método findByUserCreator() foi chamado corretamente
        verify(childRepository, times(1)).findByUserCreator(externalId);
        // Verifica se o método getProperty() foi chamado corretamente
        verify(messageProperty, times(1)).getProperty("error.child.notFound");
    }

    @Test
    void testUpdateChild_WhenChildExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";
        ChildDto childDto = new ChildDto();
        childDto.setName("Updated Child");
        childDto.setNickname("updated_child");
        childDto.setAge(8);
        childDto.setPassword("password123");

        ChildModel childModel = new ChildModel();
        childModel.setExternalId(externalId);
        childModel.setName("Original Child");
        childModel.setNickname("original_child");
        childModel.setAge(6);

        // Mock do ChildRepository
        when(childRepository.existsByExternalId(externalId)).thenReturn(true);
        when(childRepository.findByExternalId(externalId)).thenReturn(childModel);

        // Mock do método passwordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        when(securityConfigurations.passwordEncoder()).thenReturn(passwordEncoder);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.updateChild(childDto, externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespChild);
        RespChild respChild = (RespChild) response.getBody();
        assertEquals(externalId, respChild.getExternalId());
        assertEquals("Updated Child", respChild.getName());
        assertEquals("updated_child", respChild.getNickname());
        assertEquals(8, respChild.getAge());

        // Verifica se o método existsByExternalId() foi chamado corretamente
        verify(childRepository, times(1)).existsByExternalId(externalId);
        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(childRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método save() foi chamado corretamente
        verify(childRepository, times(1)).save(childModel);
        // Verifica se o método passwordEncoder() foi chamado corretamente
        verify(securityConfigurations, times(1)).passwordEncoder();
        // Verifica se o método getProperty() não foi chamado
        verify(messageProperty, never()).getProperty(anyString());
    }
    @Test
    void testUpdateChild_WhenChildDoesNotExist_ExpectNotFoundResponse() {
        // Criação dos dados de exemplo
        String externalId = "non_existing_external_id";
        ChildDto childDto = new ChildDto();

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByExternalId(externalId)).thenReturn(false);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("error.externalIdSponsor.notFound")).thenReturn("Child externalId not found");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.updateChild(childDto, externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals("Child externalId not found", messages.getMessage());

        // Verifica se o método existsByExternalId() foi chamado corretamente
        verify(childRepository, times(1)).existsByExternalId(externalId);
        // Verifica se o método findByExternalId() não foi chamado
        verify(childRepository, never()).findByExternalId(externalId);
        // Verifica se o método save() não foi chamado
        verify(childRepository, never()).save(any());
        // Verifica se o método getProperty() foi chamado corretamente
        verify(messageProperty, times(1)).getProperty("error.externalIdSponsor.notFound");
    }

    @Test
    void testDeleteChild_WhenChildExists_ExpectOkResponse() {
        // Criação dos dados de exemplo
        String externalId = "test_external_id";

        ChildModel childModel = new ChildModel();
        childModel.setExternalId(externalId);

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.findByExternalId(externalId)).thenReturn(childModel);
        doNothing().when(childRepository).delete(childModel);

        // Configuração do comportamento do mock do MessageProperty
        when(messageProperty.getProperty("ok.user.delete")).thenReturn("User deleted successfully");

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.deleteChild(externalId);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Messages);
        Messages messages = (Messages) response.getBody();
        assertEquals(HttpStatus.OK.value(), messages.getCode());
        assertEquals("User deleted successfully", messages.getMessage());

        // Verifica se o método findByExternalId() foi chamado corretamente
        verify(childRepository, times(1)).findByExternalId(externalId);
        // Verifica se o método delete() foi chamado corretamente
        verify(childRepository, times(1)).delete(childModel);
        // Verifica se o método getProperty() foi chamado corretamente
        verify(messageProperty, times(1)).getProperty("ok.user.delete");
    }
    @Test
    void testLogin_WhenSponsorLoginWithRegisteredEmailAndPassword_ExpectOkResponse() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(false);
        loginDto.setUser("sponsor@example.com");
        loginDto.setPassword("password");

        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setEmail("sponsor@example.com");
        sponsorModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByEmail(loginDto.getUser())).thenReturn(true);
        when(sponsorRepository.findByEmail(loginDto.getUser())).thenReturn(sponsorModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.login(loginDto);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespSponsor);
        RespSponsor respSponsor = (RespSponsor) response.getBody();
        assertEquals(sponsorModel.getExternalId(), respSponsor.getExternalId());
        assertEquals(sponsorModel.getEmail(), respSponsor.getEmail());
        assertEquals(sponsorModel.getName(), respSponsor.getName());

        // Verifica se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(loginDto.getUser());
        // Verifica se o método findByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByEmail(loginDto.getUser());
    }


    @Test
    void testLogin_WhenChildLoginWithRegisteredNicknameAndPassword_ExpectOkResponse() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(true);
        loginDto.setUser("child_nickname");
        loginDto.setPassword("password");

        ChildModel childModel = new ChildModel();
        childModel.setNickname("child_nickname");
        childModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByNickname(loginDto.getUser())).thenReturn(true);
        when(childRepository.findByNickname(loginDto.getUser())).thenReturn(childModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.login(loginDto);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespChild);
        RespChild respChild = (RespChild) response.getBody();
        assertEquals(childModel.getExternalId(), respChild.getExternalId());
        assertEquals(childModel.getName(), respChild.getName());
        assertEquals(childModel.getNickname(), respChild.getNickname());
        assertEquals(childModel.getAge(), respChild.getAge());

        // Verifica se o método existsByNickname() foi chamado corretamente
        verify(childRepository, times(1)).existsByNickname(loginDto.getUser());
        // Verifica se o método findByNickname() foi chamado corretamente
        verify(childRepository, times(1)).findByNickname(loginDto.getUser());
    }

    @Test
    void testLogin_WhenChildLoginWithInvalidNickname_ExpectBadRequestException() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(true);
        loginDto.setUser("invalid_nickname");
        loginDto.setPassword("password");

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByNickname(loginDto.getUser())).thenReturn(false);

        // Chamada do método a ser testado e verificação da exceção esperada
        assertThrows(BadRequest.class, () -> apiRestService.login(loginDto));

        // Verifica se o método existsByNickname() foi chamado corretamente
        verify(childRepository, times(1)).existsByNickname(loginDto.getUser());
        // Verifica que o método findByNickname() não foi chamado
        verify(childRepository, never()).findByNickname(anyString());
    }

    @Test
    void testLogin_WhenChildLoginWithInvalidPassword_ExpectUnauthorizedException() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(true);
        loginDto.setUser("child_nickname");
        loginDto.setPassword("invalid_password");

        ChildModel childModel = new ChildModel();
        childModel.setNickname("child_nickname");
        childModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByNickname(loginDto.getUser())).thenReturn(true);
        when(childRepository.findByNickname(loginDto.getUser())).thenReturn(childModel);

        // Chamada do método a ser testado e verificação da exceção esperada
        assertThrows(Unauthorized.class, () -> apiRestService.login(loginDto));

        // Verifica se o método existsByNickname() foi chamado corretamente
        verify(childRepository, times(1)).existsByNickname(loginDto.getUser());
        // Verifica se o método findByNickname() foi chamado corretamente
        verify(childRepository, times(1)).findByNickname(loginDto.getUser());
    }

    @Test
    void testLogin_WhenSponsorLoginWithInvalidUser_ExpectBadRequestException() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(false);
        loginDto.setUser("invalid_email");
        loginDto.setPassword("password");

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByEmail(loginDto.getUser())).thenReturn(false);

        // Chamada do método a ser testado e verificação da exceção esperada
        assertThrows(BadRequest.class, () -> apiRestService.login(loginDto));

        // Verifica se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(loginDto.getUser());
        // Verifica se o método findByEmail() não foi chamado
        verify(sponsorRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLogin_WhenChildLoginWithValidCredentials_ExpectOkResponse() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(true);
        loginDto.setUser("child_nickname");
        loginDto.setPassword("password");

        ChildModel childModel = new ChildModel();
        childModel.setNickname("child_nickname");
        childModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByNickname(loginDto.getUser())).thenReturn(true);
        when(childRepository.findByNickname(loginDto.getUser())).thenReturn(childModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.login(loginDto);

        // Verificações dos resultados esperados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof RespChild);
        RespChild respChild = (RespChild) response.getBody();
        assertEquals(childModel.getExternalId(), respChild.getExternalId());
        assertEquals(childModel.getName(), respChild.getName());
        assertEquals(childModel.getNickname(), respChild.getNickname());
        assertEquals(childModel.getAge(), respChild.getAge());

        // Verifica se o método existsByNickname() foi chamado corretamente
        verify(childRepository, times(1)).existsByNickname(loginDto.getUser());
        // Verifica se o método findByNickname() foi chamado corretamente
        verify(childRepository, times(1)).findByNickname(loginDto.getUser());
    }

    @Test
    void testLogin_WhenValidChildLogin_ExpectOkResponse() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(true);
        loginDto.setUser("child_nickname");
        loginDto.setPassword("password");

        ChildModel childModel = new ChildModel();
        childModel.setNickname("child_nickname");
        childModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

        // Configuração do comportamento do mock do ChildRepository
        when(childRepository.existsByNickname(loginDto.getUser())).thenReturn(true);
        when(childRepository.findByNickname(loginDto.getUser())).thenReturn(childModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.login(loginDto);

        // Verifica se a resposta é um status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica se a resposta contém os dados corretos
        RespChild respChild = (RespChild) response.getBody();
        assertNotNull(respChild);
        assertEquals(childModel.getExternalId(), respChild.getExternalId());
        assertEquals(childModel.getName(), respChild.getName());
        assertEquals(childModel.getNickname(), respChild.getNickname());
        assertEquals(childModel.getAge(), respChild.getAge());

        // Verifica se o método existsByNickname() foi chamado corretamente
        verify(childRepository, times(1)).existsByNickname(loginDto.getUser());
        // Verifica se o método findByNickname() foi chamado corretamente
        verify(childRepository, times(1)).findByNickname(loginDto.getUser());
    }

    @Test
    void testLogin_WhenValidSponsorLogin_ExpectOkResponse() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(false);
        loginDto.setUser("sponsor@example.com");
        loginDto.setPassword("password");

        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setEmail("sponsor@example.com");
        sponsorModel.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        sponsorModel.setName("John Doe");

        // Configuração do comportamento do mock do SponsorRepository
        when(sponsorRepository.existsByEmail(loginDto.getUser())).thenReturn(true);
        when(sponsorRepository.findByEmail(loginDto.getUser())).thenReturn(sponsorModel);

        // Chamada do método a ser testado
        ResponseEntity<Object> response = apiRestService.login(loginDto);

        // Verifica se a resposta é um status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica se a resposta contém os dados corretos
        RespSponsor respSponsor = (RespSponsor) response.getBody();
        assertNotNull(respSponsor);
        assertEquals(sponsorModel.getExternalId(), respSponsor.getExternalId());
        assertEquals(sponsorModel.getEmail(), respSponsor.getEmail());
        assertEquals(sponsorModel.getName(), respSponsor.getName());

        // Verifica se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(loginDto.getUser());
        // Verifica se o método findByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).findByEmail(loginDto.getUser());
    }

    @Test
    void testLogin_WhenInvalidEmailFormat_ExpectBadRequestException() {
        // Criação dos dados de exemplo
        LoginDto loginDto = new LoginDto();
        loginDto.setIsChild(false);
        loginDto.setUser("invalid_email_format");
        loginDto.setPassword("password");

        // Chamada do método a ser testado e verificação da exceção esperada
        assertThrows(BadRequest.class, () -> apiRestService.login(loginDto));
    }


}
