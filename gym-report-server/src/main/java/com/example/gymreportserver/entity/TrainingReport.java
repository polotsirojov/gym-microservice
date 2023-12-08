package com.example.gymreportserver.entity;

import com.example.gymreportserver.payload.constants.ReportType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingReport {
    @Id
    private String id;
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean status;
    private Map<Integer, Map<String, Integer>> years;
    private ReportType type;

    public TrainingReport(String trainerUsername, String trainerFirstname, String trainerLastname, Boolean status, Map<Integer, Map<String, Integer>> years, ReportType type) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstname = trainerFirstname;
        this.trainerLastname = trainerLastname;
        this.status = status;
        this.years = years;
        this.type = type;
    }
}
