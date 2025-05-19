package com.bm.travelcore.service;

import com.bm.travelcore.model.Order;
import com.bm.travelcore.model.Passenger;
import com.bm.travelcore.strategy.datacom.data.PassengerRes;

public interface PassengerService {
    Passenger storePassengerForBooking(Order order, PassengerRes passenger);
}
