package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class BaggageReq {
    @JsonProperty("Value")
    private String value;

    @JsonProperty("Session")
    private String session;
}
