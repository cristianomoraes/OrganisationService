package com.cristiano.organisation.service.bo;

import com.cristiano.organisation.service.db_entity.Contact;
import com.cristiano.organisation.service.db_entity.Organisation;
import com.cristiano.organisation.service.exceptions.OrganisationNotFoundException;
import com.cristiano.organisation.service.repository.OrganisationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Component
@Data
public class OrganisationBO {

    @Autowired
    private OrganisationRepository organisationRepository;

    /**
     * Return all organisations
     *
     * @return
     */
    public List<Organisation> getAll() {
        return organisationRepository.findAll();
    }

    /**
     * Add a new organisation
     *
     * @param organisation Organisation Data for the new organisation
     * @return Organisation saved object
     */
    public Organisation addNewOrganisation(@RequestBody Organisation organisation) {
        return organisationRepository.save(organisation);
    }

    /**
     * Return the organisation from given id
     *
     * @param id Long organisation id
     * @return Organisation object of the given id
     */
    public Organisation get(@PathVariable long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new OrganisationNotFoundException(id));
    }

    /**
     * Get all contacts of a given organisation
     *
     * @param id Long Id from organisation
     * @return Set<Contact> all contacts of the given organisation
     */
    public Set<Contact> getAllContacts(@PathVariable long id) {
        Organisation organisation = organisationRepository.findById(id)
                .orElseThrow(() -> new OrganisationNotFoundException(id));

        return organisation.getContactList();
    }

    /**
     * Update organisation details
     *
     * @param id                 Long Id of the organisation to be updated
     * @param organisationUpdate Organisation new organisation details
     * @return Organisation updated
     */
    public Organisation updateOrganisation(@PathVariable long id, @RequestBody Organisation organisationUpdate) {
        return organisationRepository.findById(id)
                .map(organisation -> {
                    organisation.setName(organisationUpdate.getName());
                    organisation.setAddress(organisationUpdate.getAddress());
                    organisation.setUrl(organisationUpdate.getUrl());
                    return organisationRepository.save(organisation);
                }).orElseThrow(() -> new OrganisationNotFoundException(id));
    }

    /**
     * Delete a organisation
     *
     * @param id Long ID of the organisation to be deleted.
     */
    public void deleteOrganisation(@PathVariable long id) {
        organisationRepository.deleteById(id);
    }

    public Organisation save(Organisation organisation) {return organisationRepository.save(organisation); }
}
