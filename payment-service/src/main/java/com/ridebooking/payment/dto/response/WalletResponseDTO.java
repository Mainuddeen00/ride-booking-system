package com.ridebooking.payment.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {

    private Long walletId;

    private Long userId;

    private BigDecimal balance;

}
