package com.ridebooking.payment.service;

import com.ridebooking.payment.dto.request.WalletRequestDTO;
import com.ridebooking.payment.dto.response.WalletResponseDTO;

public interface WalletService {

    WalletResponseDTO addMoney(WalletRequestDTO requestDTO);

    WalletResponseDTO deductMoney(WalletRequestDTO requestDTO);

    WalletResponseDTO getWalletByUserId(Long userId);

}
