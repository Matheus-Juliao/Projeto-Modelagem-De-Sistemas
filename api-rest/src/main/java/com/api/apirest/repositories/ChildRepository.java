package com.api.apirest.repositories;

import com.api.apirest.models.ChildModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<ChildModel, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByExternalId(String externalId);
    ChildModel findByNickname(String nickname);
    ChildModel findByExternalId(String externalId);
    List<ChildModel> findByUserCreator(String externalId);
}
