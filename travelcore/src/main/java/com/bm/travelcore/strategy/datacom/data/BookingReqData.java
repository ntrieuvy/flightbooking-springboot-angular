package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingReqData {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("System")
    private String system;

    @JsonProperty("RequestKey")
    private String requestKey;

    @JsonProperty("GuestContact")
    private ContactInfo guestContact;

    @JsonProperty("AgentContact")
    private ContactInfo agentContact;

    @JsonProperty("ListPassenger")
    private List<PassengerReq> listPassenger;

    @JsonProperty("ListAirOption")
    private List<AirOptionReq> listAirOptionRes;

    @JsonProperty("Option")
    private OptionBookingReq option;
}
