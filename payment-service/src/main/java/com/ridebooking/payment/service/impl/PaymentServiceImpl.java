package com.ridebooking.payment.service.impl;

import com.ridebooking.dto.PaymentRequestDTO;
import com.ridebooking.dto.PaymentResponseDTO;
import com.ridebooking.enums.PaymentStatus;
import com.ridebooking.payment.dto.gateway.PaymentGatewayResponse;
import com.ridebooking.payment.entity.Payment;
import com.ridebooking.payment.exception.ResourceNotFoundException;
import com.ridebooking.payment.gateway.PaymentGatewayService;
import com.ridebooking.payment.mapper.PaymentMapper;
import com.ridebooking.payment.repository.PaymentRepository;
import com.ridebooking.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentGatewayService paymentGatewayService;

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {

        Payment payment = paymentMapper.toEntity(requestDTO);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(null);

        // Save initial PENDING state
        payment = paymentRepository.save(payment);

        PaymentGatewayResponse gatewayResponse =
                paymentGatewayService.processPayment(requestDTO);

        if (gatewayResponse.isSuccess()) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(gatewayResponse.getTransactionId());
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setTransactionId(null);
        }

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponseDTO(savedPayment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment",
                                "paymentId",
                                paymentId));

        return paymentMapper.toResponseDTO(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePayment(Long paymentId,
                                            PaymentRequestDTO requestDTO) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment",
                                "paymentId",
                                paymentId));

        paymentMapper.updateEntity(requestDTO, payment);

        // Set to PENDING before reprocessing
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(null);
        payment = paymentRepository.save(payment);

        PaymentGatewayResponse gatewayResponse =
                paymentGatewayService.processPayment(requestDTO);

        if (gatewayResponse.isSuccess()) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(gatewayResponse.getTransactionId());
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setTransactionId(null);
        }

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponseDTO(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment",
                                "paymentId",
                                paymentId));

        paymentRepository.delete(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO refundPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment",
                                "paymentId",
                                paymentId));

        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponseDTO(updatedPayment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByUser(Long userId) {

        return paymentRepository.findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByRide(Long rideId) {

        return paymentRepository.findByRideId(rideId)
                .stream()
                .map(paymentMapper::toResponseDTO)
                .toList();
    }
}
