package com.bm.travelcore.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ContactResDTO {
    private String gender;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
