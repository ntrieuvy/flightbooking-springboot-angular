package com.bm.travelcore.repository;

import com.bm.travelcore.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByUniqueKey(String uniqueKey);
}
