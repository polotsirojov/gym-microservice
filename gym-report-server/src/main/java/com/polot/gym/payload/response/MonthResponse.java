package com.polot.gym.payload.response;

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
