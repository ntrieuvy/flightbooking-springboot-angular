package com.bm.travelcore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyResDTO {

    private Long id;
    private String code;
    private String name;
    private String phone;
    private String email;
    private String address;
    private double accountBalance;
}
