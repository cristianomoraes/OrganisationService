package com.cristiano.organisation.service;


import com.cristiano.organisation.service.db_entity.Contact;
import com.cristiano.organisation.service.db_entity.Organisation;
import com.cristiano.organisation.service.repository.ContactRepository;
import com.cristiano.organisation.service.repository.OrganisationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=false"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    private static OrganisationRepository organisationRepository;

    @BeforeAll
    static void init(@Autowired OrganisationRepository organisationRepository) throws Exception {
        ContactControllerTest.organisationRepository = organisationRepository;

        Organisation organisation = new Organisation();
        organisation.setId(1L);
        organisation.setName("CentricMinds");
        organisation.setAddress("Auckland, ACK");
        organisation.setUrl("www.centricminds.co.nz");

        organisationRepository.save(organisation);

        organisation = new Organisation();
        organisation.setId(2L);
        organisation.setName("Google");
        organisation.setAddress("USA");
        organisation.setUrl("www.google.com");

        organisationRepository.save(organisation);
    }

    @Test
    @Order(1)
    void addNewContact() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/organisations/1/contacts")
                        .contentType("application/json")
                        .content("{\"name\": \"Cristiano Moraes\", \"email\": \"cristiano.moraes@gmail.com\", \"phoneNumber\": \"027 466 9510\"}"))
                .andExpect(status().isOk());

        Organisation organisation = organisationRepository.findById(1L).get();
        assertThat(organisation.getContactList().size() == 1);
        assertThat(new ArrayList<>(organisation.getContactList()).get(0).getEmail().equals("cristiano.moraes@gmail.com"));
    }

    @Test
    @Order(2)
    void addExistingContactToOrganisation() throws Exception {
        Contact contact = contactRepository.findByName("Cristiano Moraes").get(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/organisations/2/contacts")
                        .contentType("application/json")
                        .content("{\"id\": " + contact.getId() + "}"))
                .andExpect(status().isOk());

        assertThat(contact.getOrganisations().size() == 2);
    }

    @Test
    @Order(3)
    void updateContact() throws Exception {

        Long id = contactRepository.findByName("Cristiano Moraes").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/contacts/" + id)
                        .contentType("application/json")
                        .content("{\"name\":\"Cristiano Moraes\",\"email\":\"moraescristiano@hotmail.com\",\"phoneNumber\":\"027 466 9510\"}"))
                .andExpect(status().isOk());

        Contact contact = contactRepository.findById(id).get();
        assertThat(contact.getEmail().equals("moraescristiano@hotmail.com"));
    }

    @Test
    @Order(4)
    void getContact() throws Exception {

        Long id = contactRepository.findByName("Cristiano Moraes").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/" + id)).
                andExpect(status().isOk()).
                andExpect(content().string("{\"id\":" + id + ",\"name\":\"Cristiano Moraes\",\"email\"" +
                        ":\"moraescristiano@hotmail.com\",\"phoneNumber\":\"027 466 9510\"}"));
    }

    @Test
    @Order(5)
    void deleteContactFromOrganisation() throws Exception {

        Contact contact = contactRepository.findByName("Cristiano Moraes").get(0);

        for (Organisation organisation : contact.getOrganisations()) {
            mockMvc.perform(MockMvcRequestBuilders.delete("/organisations/" + organisation.getId() +
                            "/contacts/" + contact.getId())).
                    andExpect(status().isOk());
        }
        ;

        assertThat(contact.getOrganisations().size() == 0);
    }

    @Test
    @Order(6)
    void deleteContact() throws Exception {

        Long id = contactRepository.findByName("Cristiano Moraes").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/contacts/" + id)).
                andExpect(status().isOk());

        List<Contact> contactEntityList = contactRepository.findByName("CentricMinds");
        assertThat(contactEntityList.size() == 0);
    }

    @Test
    @Order(7)
    void errorMessageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/1")).
                andExpect(status().is4xxClientError()).
                andExpect(content().string("Could not find contact 1"));
    }
}
