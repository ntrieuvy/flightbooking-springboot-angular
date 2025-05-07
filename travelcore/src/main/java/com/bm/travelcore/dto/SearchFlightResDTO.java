package com.bm.travelcore.dto;

import com.bm.travelcore.strategy.datacom.data.FlightSearchResData;
import lombok.Data;

@Data
public class SearchFlightResDTO {
   PaginationMetadataDTO paginationMetadataDTO;
   FlightSearchResData data;
}