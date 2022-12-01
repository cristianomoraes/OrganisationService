package com.cristiano.organisation.service.controller;

import com.cristiano.organisation.service.bo.ContactBO;
import com.cristiano.organisation.service.db_entity.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API functions related to Contact
 */
@RestController
@RequiredArgsConstructor
public class ContactController {

    @Autowired
    private ContactBO contactBO;

    /**
     * Get all contacts in database
     *
     * @return List<Contact>
     */
    @GetMapping("/contacts")
    List<Contact> all() {
        return contactBO.getAll();
    }

    /**
     * Get contact of the given contact id
     *
     * @param id Contact id
     * @return Contact
     */
    @GetMapping("/contacts/{id}")
    Contact oneById(@PathVariable long id) {
        return contactBO.get(id);
    }

    /**
     * Add a new contact
     *
     * @param organisationId Id of the organisation Contact is associated
     * @param contactRequest Contact data
     * @return
     */
    @PostMapping("/organisations/{organisationId}/contacts")
    Contact addContact(@PathVariable(value = "organisationId") Long organisationId
            , @RequestBody Contact contactRequest) {

        return contactBO.addContact(organisationId, contactRequest);
    }

    /**
     * Update data of a given contact
     *
     * @param id            Long id of the contact to be updated.
     * @param contactUpdate Contact object with the new contact data
     * @return
     */
    @PutMapping("/contacts/{id}")
    Contact updateContact(@PathVariable long id, @RequestBody Contact contactUpdate) {
        return contactBO.updateContact(id, contactUpdate);
    }

    /**
     * Delete contact of the given id. Only delete if contact has no connected organisation
     *
     * @param id Long Contact id
     */
    @DeleteMapping("/contacts/{id}")
    void deleteContact(@PathVariable long id) {
        contactBO.deleteContact(id);
    }

    /**
     * Remove a given contact to a given organisation
     *
     * @param organisationId
     * @param contactId
     */
    @DeleteMapping("/organisations/{organisationId}/contacts/{contactId}")
    void deleteContactFromOrganisation(@PathVariable(value = "organisationId") Long organisationId,
                                       @PathVariable(value = "contactId") Long contactId) {
        contactBO.deleteContactFromOrganisation(organisationId, contactId);
    }

}
