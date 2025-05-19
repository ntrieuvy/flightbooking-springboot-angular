package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.ContactInfoDTO;
import com.bm.travelcore.dto.OrderDetailResDTO;
import com.bm.travelcore.dto.OrderResDTO;
import com.bm.travelcore.dto.PassengerResDTO;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.populator.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderResPopulator implements Populator<Order, OrderResDTO> {

    private final OrderDetailResPopulator orderDetailPopulator;
    private final PassengerResPopulator passengerPopulator;
    private final ContactInfoResPopulator contactInfoPopulator;

    @Override
    public void populate(Order source, OrderResDTO target) {
        target.setId(source.getId());
        target.setProviderBookingId(source.getProviderBookingId());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setNote(source.getNote());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setCreatedFrom(source.getCreatedFrom());
        target.setIsSendMail(source.getIsSendMail());

        if (source.getCustomer() != null) {
            ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
            contactInfoPopulator.populate(source.getCustomer(), contactInfoDTO);
            target.setCustomer(contactInfoDTO);
        }

        if (source.getOrderDetails() != null && !source.getOrderDetails().isEmpty()) {
            target.setOrderDetails(source.getOrderDetails().stream()
                    .map(orderDetail -> {
                        OrderDetailResDTO dto = new OrderDetailResDTO();
                        orderDetailPopulator.populate(orderDetail, dto);
                        return dto;
                    })
                    .toList());
        }

        if (source.getPassengers() != null && !source.getPassengers().isEmpty()) {
            target.setPassengers(source.getPassengers().stream()
                    .map(passenger -> {
                        PassengerResDTO dto = new PassengerResDTO();
                        passengerPopulator.populate(passenger, dto);
                        return dto;
                    })
                    .toList());
        }
    }
}