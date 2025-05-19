package com.bm.travelcore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDTO {
    private Long id;
    private String providerBookingId;
    private ContactInfoDTO customer;
    private String type;
    private Integer status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdFrom;
    private Boolean isSendMail;
    private List<OrderDetailResDTO> orderDetails;
    private List<PassengerResDTO> passengers;
}
