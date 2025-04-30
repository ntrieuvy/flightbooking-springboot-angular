package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FarePax {
    @JsonProperty("PaxType")
    private String paxType;

    @JsonProperty("PaxNumb")
    private int paxNumb;

    @JsonProperty("BaseFare")
    private double baseFare;

    @JsonProperty("BaseFareOrigin")
    private double baseFareOrigin;

    @JsonProperty("NetFare")
    private double netFare;

    @JsonProperty("NetFareOrigin")
    private double netFareOrigin;

    @JsonProperty("TotalFare")
    private double totalFare;

    @JsonProperty("TotalFareOrigin")
    private double totalFareOrigin;

    @JsonProperty("ListFareItem")
    private List<FareItem> listFareItem;

    @JsonProperty("ListTaxDetail")
    private List<TaxDetail> listTaxDetail;

    @JsonProperty("ListFareInfo")
    private List<FareInfo> listFareInfo;

    @Data
    public static class TaxDetail {
        @JsonProperty("Code")
        private String code;

        @JsonProperty("Amount")
        private double amount;

        @JsonProperty("Name")
        private String name;
    }

    @Data
    public static class FareInfo {
        @JsonProperty("SegmentId")
        private String segmentId;

        @JsonProperty("StartPoint")
        private String startPoint;

        @JsonProperty("EndPoint")
        private String endPoint;

        @JsonProperty("FareClass")
        private String fareClass;

        @JsonProperty("FareBasis")
        private String fareBasis;

        @JsonProperty("HandBaggage")
        private String handBaggage;

        @JsonProperty("FreeBaggage")
        private String freeBaggage;

        @JsonProperty("Availability")
        private int availability;
    }
}
