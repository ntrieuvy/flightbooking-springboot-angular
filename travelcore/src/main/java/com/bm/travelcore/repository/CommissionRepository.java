package com.bm.travelcore.repository;

import com.bm.travelcore.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    @Query("SELECT c FROM Commission c " +
            "JOIN FETCH c.airline a " +
            "JOIN FETCH c.airportGroup " +
            "WHERE c.user.id = :userId " +
            "AND a.code = :airlineCode " +
            "AND c.airportGroup.id = :airportGroupId")
    Commission findByUserIdAndAirlineCodeAndAirportGroupId(
            @Param("userId") Long userId,
            @Param("airlineCode") String airlineCode,
            @Param("airportGroupId") Long airportGroupId);

    List<Commission> findByUserId(Long id);
}
