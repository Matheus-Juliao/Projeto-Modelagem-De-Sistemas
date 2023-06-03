package com.api.apirest.repositories;

import com.api.apirest.models.SponsorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<SponsorModel, Long> {
    boolean existsByEmail(String email);
    SponsorModel findByEmail(String email);
}