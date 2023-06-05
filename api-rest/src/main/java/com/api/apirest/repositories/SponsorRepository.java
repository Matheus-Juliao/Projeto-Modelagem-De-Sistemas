package com.api.apirest.repositories;

import com.api.apirest.models.SponsorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorRepository extends JpaRepository<SponsorModel, Long> {
    boolean existsByEmail(String email);
    SponsorModel findByEmail(String email);
    SponsorModel findByExternalId(String External);
}