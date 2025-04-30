package com.bm.travelcore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CommissionListResDTO {
    private List<CommissionDTO> commissions;
    private int count;
}