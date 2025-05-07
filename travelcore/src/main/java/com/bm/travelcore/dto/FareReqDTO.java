package com.bm.travelcore.dto;

import lombok.Data;

import java.util.List;

@Data
public class FareReqDTO {
    private Integer fareId;
    private List<FlightReqDTO> listFlight;
}
