package com.bm.travelcore.service;

import com.bm.travelcore.model.Passenger;
import com.bm.travelcore.strategy.datacom.data.BaggageRes;

public interface BaggageService {
    void storeBaggageForBooking(Passenger passengerForBooking, BaggageRes baggage);
}
