package com.polot.gym.entity;

import com.polot.gym.payload.constants.Month;
import com.polot.gym.payload.constants.ReportType;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "training_report")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainerUsername;

    @Column(nullable = false)
    private String trainerFirstname;

    @Column(nullable = false)
    private String trainerLastname;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private Month month;

    @Column(nullable = false)
    private Integer date;

    @Column(nullable = false)
    private Integer trainingDuration;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ReportType type;
}
