package com.example.gymreportserver.payload.request;

import com.example.gymreportserver.payload.constants.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReportRequest {
    @NotNull
    private String trainerUsername;
    @NotNull
    private String trainerFirstname;
    @NotNull
    private String trainerLastname;
    @NotNull
    private Boolean isActive;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private Integer trainingDuration;
    @NotNull
    private ReportType type;
}
