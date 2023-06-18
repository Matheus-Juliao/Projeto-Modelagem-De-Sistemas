package com.api.apirest;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
import com.api.apirest.repositories.ChildRepository;
import com.api.apirest.repositories.SponsorRepository;
import com.api.apirest.services.ApiRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApiRestApplicationTests {
    @Mock
    private ChildRepository childRepository;

    @Mock
    private SponsorRepository sponsorRepository;

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



}
