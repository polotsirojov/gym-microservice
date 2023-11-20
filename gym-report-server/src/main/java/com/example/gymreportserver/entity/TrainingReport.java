package com.example.gymreportserver.entity;

import com.example.gymreportserver.payload.constants.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document
@Builder
@AllArgsConstructor
public class TrainingReport {
    @Id
    private String id;
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean status;
    private Map<Integer, Map<String, Integer>> years;
    private ReportType type;
}
