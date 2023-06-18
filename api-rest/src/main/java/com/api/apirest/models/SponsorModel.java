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

    //Chave estrangeira de Sponsor e Child n:n
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sponsor_child",
            schema = "public",
            joinColumns = @JoinColumn(name = "id_sponsor", referencedColumnName = "id_sponsor"),
            inverseJoinColumns = @JoinColumn(name = "id_child", referencedColumnName = "id_child"))
    private List<ChildModel> childModels = new ArrayList<>();

    //Ligação da chave estrangeira de tarefas
    @OneToMany(mappedBy = "sponsorModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskModel> tasks = new ArrayList<>();

    //Ligação da chave estrangeira de Total
    @OneToMany(mappedBy = "sponsorModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TotalMonthlyAmountModel> totalModels = new ArrayList<>();

    //Ligação da chave estrangeira de bônus
    @OneToMany(mappedBy = "sponsorModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BonusModel> bonusModels = new ArrayList<>();

    //Ligação da chave estrangeira de penalidades
    @OneToMany(mappedBy = "sponsorModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PenaltyModel> penaltyModels = new ArrayList<>();

    public void addTotal(TotalMonthlyAmountModel total) {
        totalModels.add(total);
        total.setSponsorModel(this);
    }

    public void removeTotal(TotalMonthlyAmountModel total) {
        totalModels.remove(total);
        total.setSponsorModel(null);
    }

    public void addBonus(BonusModel bonusModel) {
        bonusModels.add(bonusModel);
        bonusModel.setSponsorModel(this);
    }

    public void addPenalty(PenaltyModel penaltyModel) {
        penaltyModels.add(penaltyModel);
        penaltyModel.setSponsorModel(this);
    }

    public void addTask(TaskModel task) {
        tasks.add(task);
        task.setSponsorModel(this);
    }

    public void removeTask(TaskModel task) {
        tasks.remove(task);
        task.setSponsorModel(null);
    }
}