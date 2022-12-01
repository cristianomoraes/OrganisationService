package com.cristiano.organisation.service.repository;

import com.cristiano.organisation.service.db_entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByName(String name);
}
