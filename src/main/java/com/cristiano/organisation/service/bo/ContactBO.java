package com.cristiano.organisation.service.bo;

import com.cristiano.organisation.service.db_entity.Contact;
import com.cristiano.organisation.service.db_entity.Organisation;
import com.cristiano.organisation.service.exceptions.ContactNotFoundException;
import com.cristiano.organisation.service.exceptions.DeleteConnectedContactException;
import com.cristiano.organisation.service.exceptions.OrganisationNotFoundException;
import com.cristiano.organisation.service.repository.ContactRepository;
import com.cristiano.organisation.service.repository.OrganisationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ContactBO {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    /**
     * Get all contacts in database
     *
     * @return List<Contact>
     */
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    /**
     * Get contact of the given contact id
     *
     * @param id Contact id
     * @return Contact
     */

    public Contact get(long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    /**
     * Add a new contact to the given organisation or add existing contact to the given organisation
     * Body is json String with concat data. If json contain contact id, it is used to find the contact.
     * If contact is found, it is connected to given organisation, otherwise a new contact is created
     *
     * @param organisationId Id of the organisation Contact is associated
     * @param contactRequest Contact data
     * @return
     */
    public Contact addContact(Long organisationId, Contact contactRequest) {

        return organisationRepository.findById(organisationId).map(organisation -> {
            long contactId = contactRequest.getId();

            if (contactId != 0L) {
                Contact _contact = contactRepository.findById(contactId)
                        .orElseThrow(() -> new ContactNotFoundException(contactId));
                organisation.addContact(_contact);
                organisationRepository.save(organisation);
                return _contact;
            }

            organisation.addContact(contactRequest);
            return contactRepository.save(contactRequest);
        }).orElseThrow(() -> new OrganisationNotFoundException(organisationId));
    }

    /**
     * Update data of a given contact
     *
     * @param id            Long id of the contact to be updated.
     * @param contactUpdate Contact object with the new contact data
     * @return
     */
    public Contact updateContact(long id, Contact contactUpdate) {

        return contactRepository.findById(id)
                .map(contact -> {
                    contact.setName(contactUpdate.getName());
                    contact.setEmail(contactUpdate.getEmail());
                    contact.setPhoneNumber(contactUpdate.getPhoneNumber());
                    return contactRepository.save(contact);
                })
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    /**
     * Delete contact of the given id. Only delete if contact has no connected organisation
     *
     * @param id Long Contact id
     */
    public void deleteContact(long id) {
        Contact contact = contactRepository.findById(id).get();
        if (contact.getOrganisations().size() > 0) {
            throw new DeleteConnectedContactException(id);
        }

        contactRepository.deleteById(id);
    }

    /**
     * Remove a given contact to a given organisation
     *
     * @param organisationId
     * @param contactId
     */
    public void deleteContactFromOrganisation(Long organisationId, Long contactId) {
        Organisation organisation = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new OrganisationNotFoundException(organisationId));

        organisation.removeContact(contactId);
        organisationRepository.save(organisation);
    }
}
