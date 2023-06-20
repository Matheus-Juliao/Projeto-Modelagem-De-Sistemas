package com.api.apirest.repositories;

import com.api.apirest.models.BonusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends JpaRepository<BonusModel, Long> {
}
