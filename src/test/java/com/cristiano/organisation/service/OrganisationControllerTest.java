package com.cristiano.organisation.service;


import com.cristiano.organisation.service.db_entity.Contact;
import com.cristiano.organisation.service.db_entity.Organisation;
import com.cristiano.organisation.service.repository.ContactRepository;
import com.cristiano.organisation.service.repository.OrganisationRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
public class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private ContactRepository contactRepositoryRepository;

    @Test
    @Order(1)
    void addNewOrganisation() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/organisations")
                        .contentType("application/json")
                        .content("{\"name\": \"CentricMinds\", \"address\": \"Auckland, AK\", \"url\": \"www.centricminds.com\"}"))
                .andExpect(status().isOk());

        List<Organisation> organisationEntityList = organisationRepository.findByName("CentricMinds");
        assertThat(organisationEntityList.size() == 1);
        assertThat(organisationEntityList.get(0).getUrl().equals("www.centricminds.com"));
    }

    @Test
    @Order(2)
    void updateOrganisation() throws Exception {

        Long id = organisationRepository.findByName("CentricMinds").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/organisations/" + id)
                        .contentType("application/json")
                        .content("{\"name\": \"CentricMinds\", \"address\": \"Auckland, AK\", \"url\": \"www.centricminds.co.nz\"}"))
                .andExpect(status().isOk());

        Organisation organisation = organisationRepository.findById(id).get();
        assertThat(organisation.getUrl().equals("www.centricminds.co.nz"));
    }

    @Test
    @Order(3)
    void getOrganisation() throws Exception {

        Long id = organisationRepository.findByName("CentricMinds").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/organisations/" + id)).
                andExpect(status().isOk()).
                andExpect(content().string("{\"id\":" + id + ",\"name\":\"CentricMinds\",\"address\":\"Auckland, AK\"," +
                        "\"url\":\"www.centricminds.co.nz\",\"contactList\":[]}"));
    }

    @Test
    @Order(4)
    void getContactList() throws Exception {
        Organisation organisation = organisationRepository.findByName("CentricMinds").get(0);

        Contact contact1 = contactRepositoryRepository.save(new Contact("Cristiano Moraes", "cristiano.moraes@gmail.com", "027 466 9510"));
        Contact contact2 = contactRepositoryRepository.save(new Contact("John Doe", "john.doe@gamil.com", "1234 5678"));

        organisation.addContact(contact1);
        organisation.addContact(contact2);
        organisationRepository.save(organisation);

        mockMvc.perform(MockMvcRequestBuilders.get("/organisations/" + organisation.getId() + "/contacts")).
                andExpect(status().isOk()).
                andExpect(content().string(
                        "[{\"id\":" + contact1.getId() + ",\"name\":\"Cristiano Moraes\",\"email\":\"cristiano.moraes@gmail.com\",\"phoneNumber\":\"027 466 9510\"}," +
                                "{\"id\":" + contact2.getId() + ",\"name\":\"John Doe\",\"email\":\"john.doe@gamil.com\",\"phoneNumber\":\"1234 5678\"}]"));
    }

    @Test
    @Order(5)
    void deleteOrganisation() throws Exception {

        Long id = organisationRepository.findByName("CentricMinds").get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/organisations/" + id)).
                andExpect(status().isOk());

        List<Organisation> organisationEntityList = organisationRepository.findByName("CentricMinds");
        assertThat(organisationEntityList.size() == 0);
    }

    @Test
    @Order(6)
    void errorMessageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/organisations/10")).
                andExpect(status().is4xxClientError()).
                andExpect(content().string("Could not find organisation 10"));
    }
}
