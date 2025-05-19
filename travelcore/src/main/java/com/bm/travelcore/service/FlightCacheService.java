package com.bm.travelcore.service;

import com.bm.travelcore.model.AirportGroup;
import com.bm.travelcore.model.Commission;

public interface FlightCacheService {
    AirportGroup findAirportGroupForFlight(String startPointCode, String endPointCode);
    Commission getCommission(Long userId, String airlineCode, Long airportGroupId);
}
