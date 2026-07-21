package com.ridebooking.payment.gateway;

import com.ridebooking.dto.PaymentRequestDTO;
import com.ridebooking.payment.dto.gateway.PaymentGatewayResponse;

public interface PaymentGatewayService {

    PaymentGatewayResponse processPayment(PaymentRequestDTO request);

}
