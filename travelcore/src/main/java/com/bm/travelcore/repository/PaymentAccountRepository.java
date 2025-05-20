package com.bm.travelcore.repository;

import com.bm.travelcore.model.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {
}
