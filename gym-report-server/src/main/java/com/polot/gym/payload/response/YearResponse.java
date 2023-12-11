package com.polot.gym.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class YearResponse {
    private Integer year;
    private List<MonthResponse> months;
}
