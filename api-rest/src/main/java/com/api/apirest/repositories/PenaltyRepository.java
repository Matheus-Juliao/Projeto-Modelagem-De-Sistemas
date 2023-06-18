package com.api.apirest.repositories;

import com.api.apirest.models.PenaltyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<PenaltyModel, Long> {
}
