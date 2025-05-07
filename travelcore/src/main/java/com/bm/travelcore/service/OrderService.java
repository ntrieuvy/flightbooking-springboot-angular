package com.bm.travelcore.service;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.strategy.datacom.data.BookingResData;

public interface OrderService {
    Order bookingFlight(BookFlightReqDTO request, BookingResData bookingResData, Agency agency, Customer customer);
}
