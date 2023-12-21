package com.polot.gym.payload.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MonthResponse {
    private String month;
    private Integer duration;
}
