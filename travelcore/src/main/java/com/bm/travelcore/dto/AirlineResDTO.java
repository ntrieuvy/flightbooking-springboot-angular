package com.bm.travelcore.dto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResDTO {
    private Long id;
    private String name;
    private String code;
    private String logo;
    private Boolean isActive;
}
