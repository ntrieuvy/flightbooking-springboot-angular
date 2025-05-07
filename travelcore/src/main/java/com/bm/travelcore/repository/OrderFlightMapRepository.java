package com.bm.travelcore.repository;

import com.bm.travelcore.model.Flight;
import com.bm.travelcore.model.OrderDetail;
import com.bm.travelcore.model.OrderFlightMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFlightMapRepository extends JpaRepository<OrderFlightMap, Long> {
    boolean existsByOrderDetailAndFlight(OrderDetail orderDetail, Flight existingFlight);
}
