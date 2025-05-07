package com.bm.travelcore.service;

import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.strategy.datacom.data.ContactInfo;

public interface CustomerService {
    Customer storeCustomer(ContactInfo guestContact, Agency agency);
}
