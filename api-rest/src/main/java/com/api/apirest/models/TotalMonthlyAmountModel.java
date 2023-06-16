package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
}