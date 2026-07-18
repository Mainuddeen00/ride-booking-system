package com.ridebooking.payment.service;

import com.ridebooking.payment.dto.request.WalletRequestDTO;
import com.ridebooking.payment.dto.response.WalletResponseDTO;
import com.ridebooking.payment.entity.Wallet;
import com.ridebooking.payment.exception.InvalidPaymentStateException;
import com.ridebooking.payment.exception.ResourceNotFoundException;
import com.ridebooking.payment.repository.WalletRepository;
import com.ridebooking.payment.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private WalletRequestDTO requestDTO;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        requestDTO = WalletRequestDTO.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .build();

        wallet = Wallet.builder()
                .walletId(10L)
                .userId(1L)
                .balance(BigDecimal.valueOf(500.0))
                .build();
    }

    @Test
    void testAddMoneyToExistingWallet() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponseDTO response = walletService.addMoney(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(BigDecimal.valueOf(600.0), response.getBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testAddMoneyToNewWallet() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet saved = invocation.getArgument(0);
            saved.setWalletId(20L);
            return saved;
        });

        WalletResponseDTO response = walletService.addMoney(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(BigDecimal.valueOf(100.0), response.getBalance());
        assertEquals(20L, response.getWalletId());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testDeductMoneySuccess() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponseDTO response = walletService.deductMoney(requestDTO);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(400.0), response.getBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testDeductMoneyInsufficientBalance() {
        wallet.setBalance(BigDecimal.valueOf(50.0));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        assertThrows(InvalidPaymentStateException.class, () -> walletService.deductMoney(requestDTO));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void testDeductMoneyWalletNotFound() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> walletService.deductMoney(requestDTO));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void testGetWalletByUserIdSuccess() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        WalletResponseDTO response = walletService.getWalletByUserId(1L);

        assertNotNull(response);
        assertEquals(10L, response.getWalletId());
        assertEquals(BigDecimal.valueOf(500.0), response.getBalance());
    }

    @Test
    void testGetWalletByUserIdNotFound() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> walletService.getWalletByUserId(1L));
    }
}
