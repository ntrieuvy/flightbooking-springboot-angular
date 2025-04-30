package com.bm.travelcore.dto;

import com.bm.travelcore.model.enums.CommissionType;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommissionQueryReqDTO {
    private String airlineCode;
    private String airportCode;
    private CommissionType commissionType;

    public CommissionQueryReqDTO() {

    }
}
