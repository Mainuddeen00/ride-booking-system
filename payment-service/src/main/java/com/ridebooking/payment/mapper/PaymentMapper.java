package com.ridebooking.payment.mapper;

import com.ridebooking.dto.PaymentRequestDTO;
import com.ridebooking.dto.PaymentResponseDTO;
import com.ridebooking.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        return Payment.builder()
                .rideId(dto.getRideId())
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }

    public PaymentResponseDTO toResponseDTO(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .rideId(payment.getRideId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentTime(payment.getPaymentTime())
                .build();
    }

    public void updateEntity(PaymentRequestDTO dto, Payment payment) {

        payment.setRideId(dto.getRideId());
        payment.setUserId(dto.getUserId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
    }

}
