package com.example.gymreportserver.payload.response;

import com.example.gymreportserver.payload.constants.Month;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthResponse {
    private String month;
    private Integer duration;
}
