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
public class OptionBookingReq {
    @JsonProperty("IssueTicket")
    private boolean issueTicket;

    @JsonProperty("SeparateBooking")
    private boolean separateBooking;

    @JsonProperty("SendEmail")
    private boolean sendEmail;
}
