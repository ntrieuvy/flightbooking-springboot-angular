package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreSeatRes {
    @JsonProperty("System")
    private String system;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("Value")
    private String value;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("PaxType")
    private String paxType;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Price")
    private double price;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Leg")
    private int leg;

    @JsonProperty("StartPoint")
    private String startPoint;

    @JsonProperty("EndPoint")
    private String endPoint;

    @JsonProperty("StatusCode")
    private String statusCode;

    @JsonProperty("Confirmed")
    private boolean confirmed;

    @JsonProperty("Session")
    private String session;
}

