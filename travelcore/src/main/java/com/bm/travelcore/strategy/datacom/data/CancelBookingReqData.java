package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class CancelBookingReqData {
    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;
    @JsonProperty("System")
    String system;
    @JsonProperty("Airline")
    String airline;
    @JsonProperty("BookingCode")
    String bookingCode;
    @JsonProperty("CancelAll")
    Boolean cancelAll;
    @JsonProperty("ListSegmentId")
    String[] listSegmentId;
}
