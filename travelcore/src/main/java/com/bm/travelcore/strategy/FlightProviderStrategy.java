package com.bm.travelcore.strategy;

import com.bm.travelcore.dto.SearchFlightReqDTO;

public interface FlightProviderStrategy {
    Object searchFlights(SearchFlightReqDTO request);
}