package com.bm.travelcore.repository;

import com.bm.travelcore.model.AirportGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportGroupRepository extends JpaRepository<AirportGroup, Long> {
    List<AirportGroup> findByAirports_Id(Long airportId);

    AirportGroup findByName(String name);
}
