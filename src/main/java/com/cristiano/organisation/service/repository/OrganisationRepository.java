package com.cristiano.organisation.service.repository;

import com.cristiano.organisation.service.db_entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    List<Organisation> findByName(String name);

}
