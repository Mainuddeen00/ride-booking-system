package com.ridebooking.payment.service;

import com.ridebooking.enums.PaymentStatus;
import com.ridebooking.payment.dto.response.InvoiceResponseDTO;
import com.ridebooking.payment.entity.Invoice;
import com.ridebooking.payment.entity.Payment;
import com.ridebooking.payment.exception.InvalidPaymentStateException;
import com.ridebooking.payment.exception.ResourceNotFoundException;
import com.ridebooking.payment.repository.InvoiceRepository;
import com.ridebooking.payment.repository.PaymentRepository;
import com.ridebooking.payment.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Payment payment;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .paymentId(1L)
                .rideId(100L)
                .userId(10L)
                .amount(BigDecimal.valueOf(150.0))
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentMethod("CARD")
                .transactionId("CARD-TXN-123")
                .paymentTime(LocalDateTime.now())
                .build();

        invoice = Invoice.builder()
                .invoiceId(5L)
                .paymentId(1L)
                .rideId(100L)
                .userId(10L)
                .amount(BigDecimal.valueOf(150.0))
                .transactionId("CARD-TXN-123")
                .generatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGenerateInvoiceSuccessNewInvoice() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(invoiceRepository.findByPaymentId(1L)).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice inv = invocation.getArgument(0);
            inv.setInvoiceId(123L);
            return inv;
        });

        InvoiceResponseDTO response = invoiceService.generateInvoice(1L);

        assertNotNull(response);
        assertEquals(123L, response.getInvoiceId());
        assertEquals(1L, response.getPaymentId());
        assertEquals(BigDecimal.valueOf(150.0), response.getAmount());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testGenerateInvoiceSuccessExistingInvoice() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(invoiceRepository.findByPaymentId(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InvoiceResponseDTO response = invoiceService.generateInvoice(1L);

        assertNotNull(response);
        assertEquals(5L, response.getInvoiceId());
        assertEquals(1L, response.getPaymentId());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testGenerateInvoicePaymentNotSuccessful() {
        payment.setPaymentStatus(PaymentStatus.FAILED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(InvalidPaymentStateException.class, () -> invoiceService.generateInvoice(1L));
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    void testGenerateInvoicePaymentNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> invoiceService.generateInvoice(1L));
    }

    @Test
    void testGetInvoiceSuccess() {
        when(invoiceRepository.findByPaymentId(1L)).thenReturn(Optional.of(invoice));

        InvoiceResponseDTO response = invoiceService.getInvoice(1L);

        assertNotNull(response);
        assertEquals(5L, response.getInvoiceId());
        assertEquals(1L, response.getPaymentId());
    }

    @Test
    void testGetInvoiceNotFound() {
        when(invoiceRepository.findByPaymentId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> invoiceService.getInvoice(1L));
    }
}
