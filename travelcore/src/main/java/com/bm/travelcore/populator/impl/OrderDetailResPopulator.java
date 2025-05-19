package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.FlightResDTO;
import com.bm.travelcore.dto.OrderDetailResDTO;
import com.bm.travelcore.dto.TicketResDTO;
import com.bm.travelcore.model.OrderDetail;
import com.bm.travelcore.populator.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDetailResPopulator implements Populator<OrderDetail, OrderDetailResDTO> {

    private final FlightResPopulator flightPopulator;
    private final TicketResPopulator ticketPopulator;

    @Override
    public void populate(OrderDetail source, OrderDetailResDTO target) {
        target.setId(source.getId());
        target.setBookingCode(source.getBookingCode());
        target.setFlightValue(source.getFlightValue());
        target.setNumberFlight(source.getNumberFlight());
        target.setStatus(source.getStatus());
        target.setIsCancel(source.getIsCancel());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setNumberAdt(source.getNumberAdt());
        target.setNumberChd(source.getNumberChd());
        target.setNumberInf(source.getNumberInf());
        target.setFareAdt(source.getFareAdt());
        target.setFareChd(source.getFareChd());
        target.setFareInf(source.getFareInf());
        target.setTaxAdt(source.getTaxAdt());
        target.setTaxChd(source.getTaxChd());
        target.setTaxInf(source.getTaxInf());
        target.setFeeAdt(source.getFeeAdt());
        target.setFeeChd(source.getFeeChd());
        target.setFeeInf(source.getFeeInf());
        target.setServiceFeeAdt(source.getServiceFeeAdt());
        target.setServiceFeeChd(source.getServiceFeeChd());
        target.setServiceFeeInf(source.getServiceFeeInf());
        target.setTotalNetPrice(source.getTotalNetPrice());
        target.setTotalServiceFee(source.getTotalServiceFee());
        target.setTotalDiscount(source.getTotalDiscount());
        target.setTotalCommission(source.getTotalCommission());
        target.setTotalPrice(source.getTotalPrice());
        target.setCurrency(source.getCurrency());
        target.setPromo(source.getPromo());
        target.setAirline(source.getAirline());
        target.setExpiryDate(source.getExpiryDate());
        target.setSession(source.getSession());
        target.setBookingImage(source.getBookingImage());
        target.setCommissionAdt(source.getCommissionAdt());
        target.setCommissionChd(source.getCommissionChd());
        target.setCommissionInf(source.getCommissionInf());
        target.setCommissionTotal(source.getCommissionTotal());
        target.setSystemCommissionAdt(source.getSystemCommissionAdt());
        target.setSystemCommissionChd(source.getSystemCommissionChd());
        target.setSystemCommissionInf(source.getSystemCommissionInf());
        target.setSystemCommissionTotal(source.getSystemCommissionTotal());
        target.setLeg(source.getLeg());

        if (source.getOrderFlightMaps() != null && !source.getOrderFlightMaps().isEmpty()) {
            target.setFlights(source.getOrderFlightMaps().stream()
                    .map(orderFlightMap -> {
                        FlightResDTO dto = new FlightResDTO();
                        flightPopulator.populate(orderFlightMap.getFlight(), dto);
                        return dto;
                    })
                    .toList());
        }

        if (source.getTickets() != null && !source.getTickets().isEmpty()) {
            target.setTickets(source.getTickets().stream()
                    .map(ticket -> {
                        TicketResDTO dto = new TicketResDTO();
                        ticketPopulator.populate(ticket, dto);
                        return dto;
                    })
                    .toList());
        }
    }
}