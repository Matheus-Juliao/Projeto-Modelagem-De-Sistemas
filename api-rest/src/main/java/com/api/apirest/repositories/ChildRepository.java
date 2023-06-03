package com.api.apirest.repositories;

import com.api.apirest.models.ChildModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<ChildModel, Long> {
    boolean existsByNickname(String nickname);
    ChildModel findByNickname (String nickname);
}
