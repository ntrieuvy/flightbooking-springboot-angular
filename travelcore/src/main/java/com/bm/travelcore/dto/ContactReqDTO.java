package com.bm.travelcore.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ContactReqDTO {
    private String gender;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
