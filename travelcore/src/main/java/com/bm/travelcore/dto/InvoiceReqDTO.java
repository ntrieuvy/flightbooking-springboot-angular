package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class InvoiceReqDTO {
    private String taxId;
    private String name;
    private String address;
    private String city;
    private String receiver;
    private String email;
}
