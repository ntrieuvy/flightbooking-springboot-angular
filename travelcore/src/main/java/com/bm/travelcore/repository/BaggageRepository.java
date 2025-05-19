package com.bm.travelcore.repository;

import com.bm.travelcore.model.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaggageRepository extends JpaRepository<Baggage, Long> {
}
