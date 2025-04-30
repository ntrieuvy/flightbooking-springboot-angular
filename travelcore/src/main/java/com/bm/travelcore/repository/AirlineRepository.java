package com.bm.travelcore.repository;

import com.bm.travelcore.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Airline findByCode(String airlineCode);
}
