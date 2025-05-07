package com.bm.travelcore.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportResDTO {
    private Long id;
    private String code;
    private String name;
}
