package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResDTO {
    private Long id;
    private String airline;
    private String ticketNumber;
    private LocalDateTime issueDate;
    private String bookingCode;
    private String passengerName;
    private String segments;
    private String ticketImage;
    private String ticketType;
    private Double totalPrice;
    private String status;
    private String errorMessage;
    private Integer itinerary;
    private String startPoint;
    private String endPoint;
    private String departDate;
    private String returnDate;
    private String fareClass;
    private String fareBasis;
    private String flightType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isSendMail;
}