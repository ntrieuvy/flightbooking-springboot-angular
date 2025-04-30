package com.bm.travelcore.dto;

import lombok.Data;

@Data
public class SearchFlightResDTO {
   PaginationMetadata paginationMetadata;
   FlightSearchDatacomResDTO data;
}