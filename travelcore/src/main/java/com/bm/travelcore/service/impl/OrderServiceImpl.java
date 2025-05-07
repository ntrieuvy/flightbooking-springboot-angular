package com.bm.travelcore.service.impl;

import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.model.Agency;
import com.bm.travelcore.model.Customer;
import com.bm.travelcore.model.Order;
import com.bm.travelcore.repository.AgencyRepository;
import com.bm.travelcore.repository.CustomerRepository;
import com.bm.travelcore.repository.OrderRepository;
import com.bm.travelcore.service.CustomerService;
import com.bm.travelcore.service.OrderService;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.strategy.datacom.data.Booking;
import com.bm.travelcore.strategy.datacom.data.BookingResData;
import com.bm.travelcore.strategy.datacom.data.ContactInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final AgencyRepository agencyRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Override
    public Order bookingFlight(BookFlightReqDTO request, BookingResData bookingResData, Agency agency, Customer customer) {
        Order order = Order.builder()
                .agency(agency)
                .customer(customer)
                .type(request.getType())
                .providerBookingId(bookingResData.getOrderId())
                .isSendMail(Boolean.TRUE)
                .user(userService.getCurrentAccount())
                .build();

        orderRepository.save(order);

        return order;
    }
}
