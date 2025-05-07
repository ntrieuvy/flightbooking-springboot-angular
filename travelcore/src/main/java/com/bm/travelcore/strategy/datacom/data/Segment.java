package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Segment {
    @JsonProperty("Leg")
    private int leg;

    @JsonProperty("SegmentId")
    private String segmentId;

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

    @JsonProperty("StartTerminal")
    private String startTerminal;

    @JsonProperty("EndTerminal")
    private String endTerminal;

    @JsonProperty("FlightNumber")
    private String flightNumber;

    @JsonProperty("Equipment")
    private String equipment;

    @JsonProperty("FareClass")
    private String fareClass;

    @JsonProperty("FareBasis")
    private String fareBasis;

    @JsonProperty("Duration")
    private int duration;

    @JsonProperty("HasStop")
    private boolean hasStop;

    @JsonProperty("StopPoint")
    private String stopPoint;

    @JsonProperty("StopTime")
    private double stopTime;

    @JsonProperty("TechnicalStop")
    private String technicalStop;

    @JsonProperty("MarriageGrp")
    private String marriageGrp;

    @JsonProperty("FlightsMiles")
    private int flightsMiles;

    @JsonProperty("Status")
    private String status;
}
