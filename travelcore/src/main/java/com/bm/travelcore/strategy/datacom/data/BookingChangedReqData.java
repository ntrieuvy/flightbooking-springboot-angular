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
public class BookingChangedReqData {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("System")
    private String system;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("BookingCode")
    private String bookingCode;

    @JsonProperty("ListPassenger")
    private List<PassengerRes> listPassenger;
}
