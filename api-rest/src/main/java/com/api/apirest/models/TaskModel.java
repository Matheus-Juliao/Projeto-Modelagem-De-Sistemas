package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TASK")
@Data
public class TaskModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_task")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;

    @Column(name = "external_id", length = 45, nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false, name = "is_complete")
    private boolean isComplete = false;

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

    //Chave estrangeira de Total 1:n
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_total", referencedColumnName = "id_total")
    private TotalMonthlyAmountModel totalModel;
}