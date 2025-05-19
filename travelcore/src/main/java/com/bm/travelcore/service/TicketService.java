package com.bm.travelcore.service;

import com.bm.travelcore.model.OrderDetail;
import com.bm.travelcore.strategy.datacom.data.TicketData;

import java.util.List;

public interface TicketService {
    void storeTicketsForBooking(List<TicketData> listTicket, OrderDetail orderDetail);
}
