package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TOTAL_MONTHLY_AMOUNT")
@Data
public class TotalMonthlyAmountModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_total")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTotal;

    @Column(name = "external_id", length = 45, nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private double remainder;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    //Chave estrangeira de Sponsor 1:n
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sponsor", referencedColumnName = "id_sponsor")
    private SponsorModel sponsorModel;

    //Chave estrangeira de Child 1:n
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_child", referencedColumnName = "id_child")
    private ChildModel childModel;

    //Ligação da chave estrangeira de Task
    @ManyToMany(mappedBy = "totalModel")
    private List<TaskModel> taskModels = new ArrayList<>();

    public void addTask(TaskModel task) {
        taskModels.add(task);
        task.setTotalModel(this);
    }

    public void removeTask(TaskModel task) {
        taskModels.remove(task);
        task.setTotalModel(null);
    }
}