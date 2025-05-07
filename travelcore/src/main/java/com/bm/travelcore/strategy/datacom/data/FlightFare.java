package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightFare {
    @JsonProperty("Leg")
    private int leg;

    @JsonProperty("Journey")
    private String journey;

    @JsonProperty("Itinerary")
    private int itinerary;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("System")
    private String system;

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("FareInfo")
    private FareInfo fareInfo;

    @JsonProperty("ListFlight")
    private List<FlightData> listFlightData;

    @JsonProperty("Adt")
    private int adt;

    @JsonProperty("Chd")
    private int chd;

    @JsonProperty("Inf")
    private int inf;

    @JsonProperty("Session")
    private String session;

    @JsonProperty("Status")
    private boolean status;

    @JsonProperty("Error")
    private String error;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("AgentId")
    private String agentId;

    @JsonProperty("AgentCode")
    private String agentCode;

    @JsonProperty("AgentType")
    private String agentType;
}
