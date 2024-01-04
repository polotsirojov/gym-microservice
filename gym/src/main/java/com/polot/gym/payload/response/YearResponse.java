package com.polot.gym.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class YearResponse {
    private Integer year;
    private List<MonthResponse> months;
}
