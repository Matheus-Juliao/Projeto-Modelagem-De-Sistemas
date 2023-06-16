package com.api.apirest;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApiRestApplicationTests {
    @Autowired
    private ApiRestService apiRestService;

    @MockBean
    private SponsorRepository sponsorRepository;
    @MockBean
    private ChildRepository childRepository;

    @Test
    public void testSaveSponsor() {
        // Criar um objeto SponsorModel de exemplo
        SponsorModel sponsor = new SponsorModel();
        sponsor.setName("Henrique Palma");
        sponsor.setEmail("henrique.p@example.com");

        when(sponsorRepository.existsByEmail(sponsor.getEmail())).thenReturn(false);
        when(sponsorRepository.save(sponsor)).thenReturn(sponsor);

        ResponseEntity<Object> result = apiRestService.saveSponsor(sponsor);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Messages messages = (Messages) result.getBody();
        assertNotNull(messages);

        // Verificar se o método existsByEmail() foi chamado corretamente
        verify(sponsorRepository, times(1)).existsByEmail(sponsor.getEmail());
        // Verificar se o método save() foi chamado corretamente
        verify(sponsorRepository, times(1)).save(sponsor);
    }
    @Test
    public void testSaveChild(){
        ChildModel childModel = new ChildModel();
        String externalIdSponsor = "external-id-aqui"; // Substitua pelo external id desejado
        childModel.setName("Jonatan M");
        childModel.setNickname("Jota");
        childModel.setPassword("testeJota");
        childModel.setUserCreator("Henrique Palma");
        childModel.setAge(10);

        when(childRepository.existsByNickname(childModel.getNickname())).thenReturn(false);
        when(sponsorRepository.findByExternalId(externalIdSponsor)).thenReturn(new SponsorModel());
        when(childRepository.save(childModel)).thenReturn(childModel);

        ResponseEntity<Object> result = apiRestService.saveChild(childModel, externalIdSponsor);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Messages messages = (Messages) result.getBody();
        assertNotNull(messages);

        verify(childRepository, times(1)).existsByNickname(childModel.getNickname());
        verify(sponsorRepository, times(1)).findByExternalId(externalIdSponsor);
        verify(childRepository, times(1)).save(childModel);
    }



}
