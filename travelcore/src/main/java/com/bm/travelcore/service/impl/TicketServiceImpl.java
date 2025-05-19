package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.OrderDetail;
import com.bm.travelcore.model.Ticket;
import com.bm.travelcore.repository.TicketRepository;
import com.bm.travelcore.service.TicketService;
import com.bm.travelcore.strategy.datacom.data.TicketData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public void storeTicketsForBooking(List<TicketData> listTicketData, OrderDetail orderDetail) {

        listTicketData.forEach(ticketData -> {
            ticketRepository.save(
                    Ticket.builder()
                            .orderDetail(orderDetail)
                            .ticketImage(ticketData.getTicketImage())
                            .airline(ticketData.getAirline())
                            .ticketNumber(ticketData.getTicketNumber())
                            .issueDate(LocalDateTime.parse(ticketData.getIssueDate()))
                            .bookingCode(ticketData.getBookingCode())
                            .passengerName(ticketData.getFullName())
                            .ticketType(ticketData.getTicketType())
                            .totalPrice(ticketData.getTotal())
                            .fareClass(ticketData.getFareClass())
                            .fareBasis(ticketData.getFareBasis())
                            .flightType(ticketData.getFlightType())
                            .departDate(ticketData.getDepartDate())
                            .itinerary(ticketData.getItinerary())
                            .startPoint(ticketData.getStartPoint())
                            .endPoint(ticketData.getEndPoint())
                            .segments(ticketData.getSegments())
                            .isSendMail(Boolean.TRUE)
                            .status(ticketData.getTicketStatus())
                            .build()
            );
        });
    }
}
