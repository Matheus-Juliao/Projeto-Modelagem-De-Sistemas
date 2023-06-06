package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPONSOR")
@Data
public class SponsorModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_sponsor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSponsor;

    @Column(name = "external_id", length = 45, nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sponsor_child",
            schema = "public",
            joinColumns = @JoinColumn(name = "id_sponsor", referencedColumnName = "id_sponsor"),
            inverseJoinColumns = @JoinColumn(name = "id_child", referencedColumnName = "id_child"))
    private List<ChildModel> childModels = new ArrayList<>();
}