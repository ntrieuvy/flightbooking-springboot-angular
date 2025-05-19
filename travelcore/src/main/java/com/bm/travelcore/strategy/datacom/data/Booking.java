package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @JsonProperty("Source")
    private String source;

    @JsonProperty("System")
    private String system;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("OrderCode")
    private String orderCode;

    @JsonProperty("OrderId")
    private String orderId;

    @JsonProperty("GdsCode")
    private String gdsCode;

    @JsonProperty("BookingCode")
    private String bookingCode;

    @JsonProperty("BookingStatus")
    private String bookingStatus;

    @JsonProperty("ExpirationTime")
    private String expirationTime;

    @JsonProperty("TimePurchase")
    private String timePurchase;

    @JsonProperty("TotalPrice")
    private double totalPrice;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("BookingPcc")
    private String bookingPcc;

    @JsonProperty("BookingSignIn")
    private String bookingSignIn;

    @JsonProperty("BookingImage")
    private String bookingImage;

    @JsonProperty("ResponseTime")
    private double responseTime;

    @JsonProperty("AutoIssue")
    private boolean autoIssue;

    @JsonProperty("Sandbox")
    private boolean sandbox;

    @JsonProperty("StatusCode")
    private String statusCode;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Warning")
    private String warning;

    @JsonProperty("GuestContact")
    private ContactInfo guestContact;

    @JsonProperty("AgentContact")
    private ContactInfo agentContact;

    @JsonProperty("ListFlightFare")
    private List<FlightFare> listFlightFare;

    @JsonProperty("ListPassenger")
    private List<PassengerRes> listPassenger;

    @JsonProperty("ListTicket")
    private List<TicketData> listTicketData;
}
