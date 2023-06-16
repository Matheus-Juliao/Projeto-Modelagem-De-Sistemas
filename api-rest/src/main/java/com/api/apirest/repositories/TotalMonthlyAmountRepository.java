package com.api.apirest.repositories;

import com.api.apirest.models.ChildModel;
import com.api.apirest.models.TotalMonthlyAmountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalMonthlyAmountRepository extends JpaRepository<TotalMonthlyAmountModel, Long> {

    TotalMonthlyAmountModel findByExternalId(String externalId);
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM total_monthly_amount WHERE id_sponsor = :id_sponsor", nativeQuery = true)
    boolean existsByTotal(@Param("id_sponsor") Long idSponsor);
}
