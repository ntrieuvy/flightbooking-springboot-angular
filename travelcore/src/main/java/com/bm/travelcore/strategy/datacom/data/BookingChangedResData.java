package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class BookingChangedResData {
    @JsonProperty("StatusCode")
    private String statusCode;

    @JsonProperty("Success")
    private boolean success;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("RequestID")
    private int requestID;

    @JsonProperty("OrderId")
    private String orderId;

    @JsonProperty("Booking")
    private Booking booking;
}
