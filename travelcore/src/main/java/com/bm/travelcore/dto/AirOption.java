package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AirOption {
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
        private List<Flight> listFlight;

        @Data
        public static class Flight {
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

            @Data
            public static class Segment {
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
        }
    }
}
