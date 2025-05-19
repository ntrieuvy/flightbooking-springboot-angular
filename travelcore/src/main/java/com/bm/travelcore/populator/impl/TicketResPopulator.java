package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.TicketResDTO;
import com.bm.travelcore.model.Ticket;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class TicketResPopulator implements Populator<Ticket, TicketResDTO> {

    @Override
    public void populate(Ticket source, TicketResDTO target) {
        target.setId(source.getId());
        target.setAirline(source.getAirline());
        target.setTicketNumber(source.getTicketNumber());
        target.setIssueDate(source.getIssueDate());
        target.setBookingCode(source.getBookingCode());
        target.setPassengerName(source.getPassengerName());
        target.setSegments(source.getSegments());
        target.setTicketImage(source.getTicketImage());
        target.setTicketType(source.getTicketType());
        target.setTotalPrice(source.getTotalPrice());
        target.setStatus(source.getStatus());
        target.setErrorMessage(source.getErrorMessage());
        target.setItinerary(source.getItinerary());
        target.setStartPoint(source.getStartPoint());
        target.setEndPoint(source.getEndPoint());
        target.setDepartDate(source.getDepartDate());
        target.setReturnDate(source.getReturnDate());
        target.setFareClass(source.getFareClass());
        target.setFareBasis(source.getFareBasis());
        target.setFlightType(source.getFlightType());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setIsSendMail(source.getIsSendMail());
    }
}