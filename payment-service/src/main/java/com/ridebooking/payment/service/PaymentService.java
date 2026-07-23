package com.ridebooking.payment.service;

import com.ridebooking.dto.PaymentRequestDTO;
import com.ridebooking.dto.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO getPaymentById(Long paymentId);

    PaymentResponseDTO updatePayment(Long paymentId,
                                     PaymentRequestDTO paymentRequestDTO);

    void deletePayment(Long paymentId);

    // New Business APIs

    PaymentResponseDTO refundPayment(Long paymentId);

    List<PaymentResponseDTO> getPaymentsByUser(Long userId);

    List<PaymentResponseDTO> getPaymentsByRide(Long rideId);

}
