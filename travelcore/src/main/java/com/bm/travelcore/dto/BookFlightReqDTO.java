package com.bm.travelcore.dto;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
@Getter
public class BookFlightReqDTO {
    private String orderId;
    private String session;
    private String type;
    private List<AirOptionReqDTO> listAirOption;
    private List<PassengerReqDTO> listPassenger;
    private ContactReqDTO contact;
    private InvoiceReqDTO invoice;
    private String paymentType;
}