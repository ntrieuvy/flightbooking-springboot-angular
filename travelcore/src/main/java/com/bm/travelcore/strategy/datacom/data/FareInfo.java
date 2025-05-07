package com.bm.travelcore.strategy.datacom.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FareInfo {
    @JsonProperty("OptionId")
    private int optionId;

    @JsonProperty("FareClass")
    private String fareClass;

    @JsonProperty("FareBasis")
    private String fareBasis;

    @JsonProperty("FareFamily")
    private String fareFamily;

    @JsonProperty("CabinCode")
    private String cabinCode;

    @JsonProperty("CabinName")
    private String cabinName;

    @JsonProperty("Refundable")
    private boolean refundable;

    @JsonProperty("Availability")
    private int availability;

    @JsonProperty("Unavailable")
    private boolean unavailable;

    @JsonProperty("ExpiryDate")
    private String expiryDate;

    @JsonProperty("BaseFare")
    private double baseFare;

    @JsonProperty("BaseFareOrigin")
    private double baseFareOrigin;

    @JsonProperty("PriceAdt")
    private double priceAdt;

    @JsonProperty("PriceAdtOrigin")
    private double priceAdtOrigin;

    @JsonProperty("NetFare")
    private double netFare;

    @JsonProperty("NetFareOrigin")
    private double netFareOrigin;

    @JsonProperty("TotalFare")
    private double totalFare;

    @JsonProperty("TotalFareOrigin")
    private double totalFareOrigin;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Airline")
    private String airline;

    @JsonProperty("System")
    private String system;

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Tourcode")
    private String tourcode;

    @JsonProperty("ListFarePax")
    private List<FarePax> listFarePax;
}
