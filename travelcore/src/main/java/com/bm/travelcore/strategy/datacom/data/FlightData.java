package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightData {
    @JsonProperty("Leg")
    private int leg;

    @JsonProperty("FlightId")
    private String flightId;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("Operator")
    private String operator;

    @JsonProperty("StartPoint")
    private String startPoint;

    @JsonProperty("EndPoint")
    private String endPoint;

    @JsonProperty("StartDate")
    private String startDate;

    @JsonProperty("EndDate")
    private String endDate;

    @JsonProperty("DepartDate")
    private String departDate;

    @JsonProperty("ArriveDate")
    private String arriveDate;

    @JsonProperty("FlightNumber")
    private String flightNumber;

    @JsonProperty("StopNum")
    private int stopNum;

    @JsonProperty("Duration")
    private int duration;

    @JsonProperty("ListSegment")
    private List<Segment> listSegment;
}
