package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ticket {
    @JsonProperty("Index")
    private int index;

    @JsonProperty("System")
    private String system;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("BookingCode")
    private String bookingCode;

    @JsonProperty("ConjTktNum")
    private String conjTktNum;

    @JsonProperty("TicketNumber")
    private String ticketNumber;

    @JsonProperty("TicketType")
    private String ticketType;

    @JsonProperty("TicketStatus")
    private String ticketStatus;

    @JsonProperty("TicketRelated")
    private String ticketRelated;

    @JsonProperty("RelatedType")
    private String relatedType;

    @JsonProperty("ServiceType")
    private String serviceType;

    @JsonProperty("ServiceCode")
    private String serviceCode;

    @JsonProperty("PaxType")
    private String paxType;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("GivenName")
    private String givenName;

    @JsonProperty("Surname")
    private String surname;

    @JsonProperty("NameId")
    private String nameId;

    @JsonProperty("Fare")
    private double fare;

    @JsonProperty("Tax")
    private double tax;

    @JsonProperty("Fee")
    private double fee;

    @JsonProperty("Vat")
    private double vat;

    @JsonProperty("Total")
    private double total;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Itinerary")
    private int itinerary;

    @JsonProperty("StartPoint")
    private String startPoint;

    @JsonProperty("EndPoint")
    private String endPoint;

    @JsonProperty("DepartDate")
    private String departDate;

    @JsonProperty("ReturnDate")
    private String returnDate;

    @JsonProperty("FareClass")
    private String fareClass;

    @JsonProperty("FareBasis")
    private String fareBasis;

    @JsonProperty("FlightType")
    private String flightType;

    @JsonProperty("Segments")
    private String segments;

    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("TicketImage")
    private String ticketImage;

    @JsonProperty("IssueDate")
    private String issueDate;
}
