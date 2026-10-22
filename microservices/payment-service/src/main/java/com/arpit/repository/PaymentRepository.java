package com.arpit.repository;

import com.arpit.modal.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentOrder , Long>{

    PaymentOrder  findByPaymentLinkId(String paymentLinkId);
}
