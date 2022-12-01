package com.cristiano.organisation.service.db_entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "organisation")
@NoArgsConstructor
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    private String url;

    public Organisation(String name, String address, String url) {
        this.name = name;
        this.address = address;
        this.url = url;
    }

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "organisation_contact",
            joinColumns = @JoinColumn(name = "organisation_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private Set<Contact> contactList = new HashSet<>();

    /**
     * Add a contact to this organisation
     *
     * @param contact
     */
    public void addContact(Contact contact) {
        this.getContactList().add(contact);
        contact.getOrganisations().add(this);
    }

    /**
     * Remove a contact to this organisation
     *
     * @param contactId
     */
    public void removeContact(Long contactId) {
        Contact contact = this.contactList.stream().filter(c -> c.getId() == contactId).findFirst().orElse(null);
        if (contact != null) {
            this.contactList.remove(contact);
            contact.getOrganisations().remove(this);
        }
    }

}
