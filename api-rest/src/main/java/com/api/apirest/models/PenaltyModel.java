package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "PENALTY")
@Data
public class PenaltyModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_penalties")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPenalties;

    @Column(name = "external_id", length = 45, nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private double penalty;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sponsor", referencedColumnName = "id_sponsor")
    private SponsorModel sponsorModel;
}