package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.ContactInfoDTO;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class ContactInfoResPopulator implements Populator<Customer, ContactInfoDTO> {

    @Override
    public void populate(Customer source, ContactInfoDTO target) {
        target.setFullName(source.getFullName());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setAddress(source.getAddress());
    }
}