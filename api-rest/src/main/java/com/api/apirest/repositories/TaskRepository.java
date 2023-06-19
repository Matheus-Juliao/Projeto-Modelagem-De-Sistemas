package com.api.apirest.repositories;

import com.api.apirest.models.TaskModel;
import com.api.apirest.models.TotalMonthlyAmountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    boolean existsByTotalModel(TotalMonthlyAmountModel totalModel);
}