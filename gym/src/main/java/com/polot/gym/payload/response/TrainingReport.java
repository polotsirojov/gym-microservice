package com.polot.gym.payload.response;

import lombok.Data;

import java.util.Map;

@Data
public class TrainingReport {
    private String id;
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean status;
    private Map<Integer, Map<String, Integer>> years;
    private String type;
}
