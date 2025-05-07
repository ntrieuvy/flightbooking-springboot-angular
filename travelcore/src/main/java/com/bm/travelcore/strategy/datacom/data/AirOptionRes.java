package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AirOptionRes {
    @JsonProperty("OptionId")
    private int optionId;

    @JsonProperty("Leg")
    private int leg;

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

    @JsonProperty("ListFlightOption")
    private List<FlightOption> listFlightOption;

    @JsonProperty("ListFareOption")
    private List<FareOption> listFareOption;

    @Data
    public static class FlightOption {
        @JsonProperty("OptionId")
        private int optionId;

        @JsonProperty("ListFlight")
        private List<FlightData> listFlightData;
    }
}
