package com.bm.travelcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AirportDTO {
    private Long id;

    @NotBlank(message = "Code is required")
    @Size(max = 255, message = "Code must be less than 255 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Group ID is required")
    private Long groupId;
}