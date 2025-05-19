package com.bm.travelcore.strategy;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.strategy.datacom.data.BookingChangedReqData;
import com.bm.travelcore.strategy.datacom.data.CancelBookingReqData;

public interface FlightProviderStrategy {
    Object searchFlights(SearchFlightReqDTO request);

    Object bookFlights(BookFlightReqDTO request);

    Object changPassengerForOrder(BookingChangedReqData changedReqData);

    Object cancelFlights(CancelBookingReqData cancelBookingReqData);
}