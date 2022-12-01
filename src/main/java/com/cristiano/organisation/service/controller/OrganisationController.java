package com.cristiano.organisation.service.controller;

import com.cristiano.organisation.service.bo.OrganisationBO;
import com.cristiano.organisation.service.db_entity.Contact;
import com.cristiano.organisation.service.db_entity.Organisation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class OrganisationController {

    @Autowired
    private OrganisationBO organisationBO;

    /**
     * Return all organisations
     *
     * @return
     */
    @GetMapping("/organisations")
    List<Organisation> all() {
        return organisationBO.getAll();
    }

    /**
     * Add a new organisation
     *
     * @param organisation Organisation Data for the new organisation
     * @return Organisation saved object
     */
    @PostMapping("/organisations")
    Organisation newOrganisation(@RequestBody Organisation organisation) {
        return organisationBO.save(organisation);
    }

    /**
     * Return the organisation from given id
     *
     * @param id Long organisation id
     * @return Organisation object of the given id
     */
    @GetMapping("/organisations/{id}")
    Organisation oneById(@PathVariable long id) {
        return organisationBO.get(id);
    }

    /**
     * Get all contacts of a given organisation
     *
     * @param id Long Id from organisation
     * @return Set<Contact> all contacts of the given organisation
     */
    @GetMapping("/organisations/{id}/contacts")
    Set<Contact> allContacts(@PathVariable long id) {
        return organisationBO.getAllContacts(id);
    }

    /**
     * Update organisation details
     *
     * @param id                 Long Id of the organisation to be updated
     * @param organisationUpdate Organisation new organisation details
     * @return Organisation updated
     */
    @PutMapping("/organisations/{id}")
    Organisation updateOrganisation(@PathVariable long id, @RequestBody Organisation organisationUpdate) {
        return organisationBO.updateOrganisation(id, organisationUpdate);
    }

    /**
     * Delete a organisation
     *
     * @param id Long ID of the organisation to be deleted.
     */
    @DeleteMapping("/organisations/{id}")
    void deleteOrganisation(@PathVariable long id) {
        organisationBO.deleteOrganisation(id);
    }

}
