package com.bm.travelcore.repository;

import com.bm.travelcore.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByCode(String code);

}
