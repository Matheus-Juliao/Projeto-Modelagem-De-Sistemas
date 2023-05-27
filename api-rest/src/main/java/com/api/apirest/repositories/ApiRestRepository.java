package com.api.apirest.repositories;

import com.api.apirest.models.ApiRestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiRestRepository extends JpaRepository<ApiRestModel, Long> {

    boolean existsByEmail(String email);

    @Query(
            value = "SELECT * FROM tb_user u WHERE u.email = :email",
            nativeQuery = true)
    ApiRestModel findByEmail(String email);
}