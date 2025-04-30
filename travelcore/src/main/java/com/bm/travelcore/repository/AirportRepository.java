package com.bm.travelcore.repository;

import com.bm.travelcore.model.Airport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    @EntityGraph(attributePaths = {"airportGroup"})
    Airport findByCode(String airportCode);

    List<Airport> findByCodeIn(Set<String> strings);

//    List<Airport> findByGroupIdAndCodeIn(long l, ArrayList<String> strings);
}
