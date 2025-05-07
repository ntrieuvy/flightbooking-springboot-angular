package com.bm.travelcore.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AirportReqDTO {
    private Long id;
    private String code;
    private String name;
}
