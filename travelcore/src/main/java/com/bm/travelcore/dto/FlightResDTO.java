package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResDTO {
    private Long id;
    private String uniqueKey;
    private String code;
    private String airline;
    private String departure;
    private String arrival;
    private String date;
    private String std;
    private String sta;
    private String number;
    private Integer duration;
    private String groupClass;
    private String fareClass;
    private Boolean promo;
    private String segments;
    private String handBaggage;
    private String allowanceBaggage;
    private String operating;
}