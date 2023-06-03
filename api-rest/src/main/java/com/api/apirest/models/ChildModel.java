package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CHILD")
@Data
public class ChildModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_child")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChild;

    @Column(name = "external_id", length = 45, nullable = false, unique = true)
    private String externalId;

    @Column(name = "external_id_sponsor", length = 45, nullable = false, unique = true)
    private String externalIdSponsor;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 2)
    private int age;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToMany(mappedBy = "childModels")
    private List<SponsorModel> sponsorModels;
}
