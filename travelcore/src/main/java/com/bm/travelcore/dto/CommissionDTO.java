package com.bm.travelcore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommissionDTO {
    private String airlineCode;
    private String airlineName;
    private String airportGroupName;
    private List<String> airportCodes;
    private double serviceFeeAdt;
    private double serviceFeeChd;
    private double serviceFeeInf;
}