package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AirOptionReq {
    @JsonProperty("Session")
    private String session;

    @JsonProperty("SessionType")
    private String sessionType;

    @JsonProperty("AirlineOptionId")
    private int airlineOptionId;

    @JsonProperty("FareOptionId")
    private int fareOptionId;

    @JsonProperty("FlightOptionId")
    private int flightOptionId;

    @JsonProperty("Tourcode")
    private String tourcode;

    @JsonProperty("CAcode")
    private String cAcode;

    @JsonProperty("VIPText")
    private String vipText;

    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("AccountCode")
    private String accountCode;
}
