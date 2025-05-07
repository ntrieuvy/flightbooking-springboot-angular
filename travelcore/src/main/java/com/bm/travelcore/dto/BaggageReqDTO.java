package com.bm.travelcore.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class BaggageReqDTO {
    private String airline;
    private String code;
    private String currency;
    private String name;
    private String route;
    private String value;
    private Integer leg;
    private Integer price;
}
