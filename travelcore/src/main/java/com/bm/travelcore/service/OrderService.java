package com.bm.travelcore.service;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.dto.OrderResDTO;
import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.strategy.datacom.data.BookingResData;

import java.util.List;

public interface OrderService {
    Order bookingFlight(BookFlightReqDTO request, BookingResData bookingResData, Agency agency, Customer customer);

    List<OrderResDTO> getUserOrders(String userId);

    OrderResDTO getOrderDetails(Long orderId);

    void cancelOrder(Long orderId);
}
