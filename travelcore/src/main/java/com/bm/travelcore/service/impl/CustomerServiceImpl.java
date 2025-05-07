package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.repository.CustomerRepository;
import com.bm.travelcore.service.CustomerService;
import com.bm.travelcore.strategy.datacom.data.ContactInfo;
import com.bm.travelcore.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final Helper helper;
    private final CustomerRepository customerRepository;

    @Override
    public Customer storeCustomer(ContactInfo guestContact, Agency agency) {
        String[] nameParts = guestContact.getName().split(" ");
        String lastName = nameParts[0];
        String firstName = nameParts[nameParts.length - 1];

        Customer customer = Customer
                .builder()
                .fullName(guestContact.getName())
                .firstName(firstName)
                .lastName(lastName)
                .email(guestContact.getEmail())
                .phone(guestContact.getPhone())
                .gender(helper.getGenderByTitle(guestContact.getTitle()))
                .address(guestContact.getAddress())
                .note(guestContact.getRemark())
                .agency(agency)
                .build();

        customerRepository.save(customer);
        return customer;
    }
}
