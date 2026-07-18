package com.ridebooking.payment.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {

    private Long invoiceId;

    private Long paymentId;

    private Long rideId;

    private Long userId;

    private BigDecimal amount;

    private String transactionId;

    private LocalDateTime generatedAt;
}
