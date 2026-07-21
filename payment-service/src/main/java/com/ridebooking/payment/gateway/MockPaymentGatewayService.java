package com.ridebooking.payment.gateway;

import com.ridebooking.payment.dto.gateway.PaymentGatewayResponse;
import com.ridebooking.dto.PaymentRequestDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MockPaymentGatewayService implements PaymentGatewayService {

    @Override
    public PaymentGatewayResponse processPayment(PaymentRequestDTO request) {

        String method = request.getPaymentMethod();

        if ("CASH".equalsIgnoreCase(method)) {
            return PaymentGatewayResponse.builder()
                    .success(true)
                    .transactionId("CASH-" + UUID.randomUUID())
                    .gatewayMessage("Cash payment successful")
                    .build();
        }

        if ("WALLET".equalsIgnoreCase(method)) {
            return PaymentGatewayResponse.builder()
                    .success(true)
                    .transactionId("WLT-" + UUID.randomUUID())
                    .gatewayMessage("Wallet payment successful")
                    .build();
        }

        if ("CARD".equalsIgnoreCase(method)) {
            if (request.getAmount().doubleValue() <= 10000) {
                return PaymentGatewayResponse.builder()
                        .success(true)
                        .transactionId("CARD-" + UUID.randomUUID())
                        .gatewayMessage("Card payment successful")
                        .build();
            } else {
                return PaymentGatewayResponse.builder()
                        .success(false)
                        .transactionId(null)
                        .gatewayMessage("Card payment failed: Limit exceeded")
                        .build();
            }
        }

        return PaymentGatewayResponse.builder()
                .success(false)
                .transactionId(null)
                .gatewayMessage("Unknown payment method: " + method)
                .build();
    }
}
