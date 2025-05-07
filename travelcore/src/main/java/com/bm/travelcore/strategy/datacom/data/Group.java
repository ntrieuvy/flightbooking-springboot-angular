package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Group {
    @JsonProperty("Leg")
    private int leg;

    @JsonProperty("TripType")
    private String tripType;

    @JsonProperty("Journey")
    private String journey;

    @JsonProperty("StartPoint")
    private String startPoint;

    @JsonProperty("EndPoint")
    private String endPoint;

    @JsonProperty("DepartDate")
    private String departDate;

    @JsonProperty("ListAirOption")
    private List<AirOptionRes> listAirOptionRes;
}