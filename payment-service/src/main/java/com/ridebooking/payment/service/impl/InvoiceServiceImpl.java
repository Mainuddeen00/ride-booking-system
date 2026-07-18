package com.ridebooking.payment.service.impl;

import com.ridebooking.payment.dto.response.InvoiceResponseDTO;
import com.ridebooking.payment.entity.Invoice;
import com.ridebooking.payment.entity.Payment;
import com.ridebooking.enums.PaymentStatus;
import com.ridebooking.payment.exception.InvalidPaymentStateException;
import com.ridebooking.payment.exception.ResourceNotFoundException;
import com.ridebooking.payment.repository.InvoiceRepository;
import com.ridebooking.payment.repository.PaymentRepository;
import com.ridebooking.payment.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceServiceImpl implements InvoiceService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public InvoiceResponseDTO generateInvoice(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment",
                                "paymentId",
                                paymentId));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidPaymentStateException(
                    "Invoice can only be generated for successful payments.");
        }

        Invoice invoice = invoiceRepository.findByPaymentId(paymentId)
                .orElse(
                        Invoice.builder()
                                .paymentId(payment.getPaymentId())
                                .rideId(payment.getRideId())
                                .userId(payment.getUserId())
                                .amount(payment.getAmount())
                                .transactionId(payment.getTransactionId())
                                .generatedAt(LocalDateTime.now())
                                .build()
                );

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return InvoiceResponseDTO.builder()
                .invoiceId(savedInvoice.getInvoiceId())
                .paymentId(savedInvoice.getPaymentId())
                .rideId(savedInvoice.getRideId())
                .userId(savedInvoice.getUserId())
                .amount(savedInvoice.getAmount())
                .transactionId(savedInvoice.getTransactionId())
                .generatedAt(savedInvoice.getGeneratedAt())
                .build();
    }

    @Override
    public InvoiceResponseDTO getInvoice(Long paymentId) {

        Invoice invoice = invoiceRepository.findByPaymentId(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice",
                                "paymentId",
                                paymentId));

        return InvoiceResponseDTO.builder()
                .invoiceId(invoice.getInvoiceId())
                .paymentId(invoice.getPaymentId())
                .rideId(invoice.getRideId())
                .userId(invoice.getUserId())
                .amount(invoice.getAmount())
                .transactionId(invoice.getTransactionId())
                .generatedAt(invoice.getGeneratedAt())
                .build();
    }
}
