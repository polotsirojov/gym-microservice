package com.polot.gym.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean status;
    private List<YearResponse> years;
}
