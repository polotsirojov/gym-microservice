package com.example.gymreportserver.repository.projection;

public interface CustomTrainingReport {
    String getTrainerUsername();
    String getTrainerFirstname();
    String getTrainerLastname();
    Boolean getIsActive();
    Integer getYear();
    String getMonth();
    Integer getDurationSummary();
}
