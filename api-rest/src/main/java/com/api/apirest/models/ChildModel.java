package com.api.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(nullable = false, length = 2)
    private int age;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "user_creator", nullable = false)
    private String userCreator;

    //Ligação da chave estrangeira de Sponsor
    @ManyToMany(mappedBy = "childModels")
    private List<SponsorModel> sponsorModels = new ArrayList<>();

    //Ligação da chave estrangeira de Total
    @ManyToMany(mappedBy = "childModel")
    private List<TotalMonthlyAmountModel> totalModels = new ArrayList<>();

    public void addTotal(TotalMonthlyAmountModel total) {
        totalModels.add(total);
        total.setChildModel(this);
    }

    public void removeTotal(TotalMonthlyAmountModel total) {
        totalModels.remove(total);
        total.setChildModel(null);
    }
}
