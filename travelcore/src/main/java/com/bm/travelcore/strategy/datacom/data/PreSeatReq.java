package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreSeatReq {
    @JsonProperty("Value")
    private String value;

    @JsonProperty("Session")
    private String session;
}