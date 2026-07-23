package com.ridebooking.payment.service.impl;

import com.ridebooking.payment.dto.request.WalletRequestDTO;
import com.ridebooking.payment.dto.response.WalletResponseDTO;
import com.ridebooking.payment.entity.Wallet;
import com.ridebooking.payment.exception.InvalidPaymentStateException;
import com.ridebooking.payment.exception.ResourceNotFoundException;
import com.ridebooking.payment.repository.WalletRepository;
import com.ridebooking.payment.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public WalletResponseDTO addMoney(WalletRequestDTO requestDTO) {

        Wallet wallet = walletRepository.findByUserId(requestDTO.getUserId())
                .orElse(
                        Wallet.builder()
                                .userId(requestDTO.getUserId())
                                .balance(BigDecimal.ZERO)
                                .build()
                );

        wallet.setBalance(
                wallet.getBalance().add(requestDTO.getAmount())
        );

        Wallet savedWallet = walletRepository.save(wallet);

        return WalletResponseDTO.builder()
                .walletId(savedWallet.getWalletId())
                .userId(savedWallet.getUserId())
                .balance(savedWallet.getBalance())
                .build();
    }

    @Override
    @Transactional
    public WalletResponseDTO deductMoney(WalletRequestDTO requestDTO) {

        Wallet wallet = walletRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wallet",
                                "userId",
                                requestDTO.getUserId()));

        if (wallet.getBalance().compareTo(requestDTO.getAmount()) < 0) {
            throw new InvalidPaymentStateException(
                    "Insufficient wallet balance.");
        }

        wallet.setBalance(
                wallet.getBalance().subtract(requestDTO.getAmount())
        );

        Wallet savedWallet = walletRepository.save(wallet);

        return WalletResponseDTO.builder()
                .walletId(savedWallet.getWalletId())
                .userId(savedWallet.getUserId())
                .balance(savedWallet.getBalance())
                .build();
    }

    @Override
    public WalletResponseDTO getWalletByUserId(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wallet",
                                "userId",
                                userId));

        return WalletResponseDTO.builder()
                .walletId(wallet.getWalletId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .build();
    }
}
