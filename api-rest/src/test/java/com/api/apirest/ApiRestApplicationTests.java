package com.api.apirest;

import com.api.apirest.configurations.MessageProperty;
import com.api.apirest.messages.Messages;
import com.api.apirest.models.ChildModel;
import com.api.apirest.models.SponsorModel;
import com.api.apirest.repositories.ChildRepository;
import com.api.apirest.repositories.SponsorRepository;
import com.api.apirest.services.ApiRestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ApiRestApplicationTests {
    @Autowired
    private ApiRestService apiRestService;

    @MockBean
    private SponsorRepository sponsorRepository;
    @MockBean
    private ChildRepository childRepository;

    @MockBean
    private MessageProperty messageProperty;
    @Test
    public void testCreateSponsor_WhenEmailNotExists_ExpectCreated() {
        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setPassword("123");
        sponsorModel.setEmail("test@example.com");
        sponsorModel.setName("Test Sponsor...");

        when(sponsorRepository.existsByEmail(sponsorModel.getEmail())).thenReturn(false);

        ResponseEntity<Object> response = apiRestService.createSponsor(sponsorModel);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertEquals("successfully registered user", messages.getMessage());
    }

    @Test
    public void testCreateSponsor_WhenEmailExists_ExpectBadRequest() {
        SponsorModel sponsorModel = new SponsorModel();
        sponsorModel.setEmail("test@example.com");
        sponsorModel.setName("Test Sponsor");

        when(sponsorRepository.existsByEmail(sponsorModel.getEmail())).thenReturn(true);

        ResponseEntity<Object> response = apiRestService.createSponsor(sponsorModel);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertEquals("This email is already linked to an account. Register another email", messages.getMessage());
    }

    @Test
    public void testCreateChild_WhenChildNotExists_ExpectOk() {
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
        assertEquals("successfully registered user", messages.getMessage());
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
        assertEquals("This nickname is already linked to an account. Register another nickname", messages.getMessage());
    }

    @Test
    public void testCreateChild_WhenSponsorNotFound_ExpectNotFound() {
        ChildModel childModel = new ChildModel();
        childModel.setNickname("test_nickname");

        when(childRepository.existsByNickname(childModel.getNickname())).thenReturn(false);
        when(sponsorRepository.findByExternalId("test_external_id")).thenReturn(null);
        when(messageProperty.getProperty("error.externalIdSponsor.notFound")).thenReturn("Sponsor not found");

        ResponseEntity<Object> response = apiRestService.createChild(childModel, "test_external_id");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Messages messages = (Messages) response.getBody();
        assertNotNull(messages.getMessage());
        assertEquals("Sponsor not found", messages.getMessage());
        //assertEquals("error.externalIdSponsor.notFound", messages.getMessage());
    }



}
