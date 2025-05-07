package com.bm.travelcore.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class BookFlightResDTO {
    public String message;
    public String code;
}
