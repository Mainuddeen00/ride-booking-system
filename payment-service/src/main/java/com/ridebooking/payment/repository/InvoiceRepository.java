package com.ridebooking.payment.repository;

import com.ridebooking.payment.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByPaymentId(Long paymentId);

}
