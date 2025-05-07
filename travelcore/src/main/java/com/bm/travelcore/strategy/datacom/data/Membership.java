package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Membership {
    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("MembershipID")
    private String membershipID;

    @JsonProperty("MembershipType")
    private String membershipType;
}
