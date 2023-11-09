package com.polot.gym.payload.request;

import com.polot.gym.payload.constants.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    private String trainingDate;
    @NotNull
    private Integer trainingDuration;
    @NotNull
    private ReportType type;
}
