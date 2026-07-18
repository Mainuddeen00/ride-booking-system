package com.ridebooking.payment.controller;

import com.ridebooking.payment.common.ApiResponse;
import com.ridebooking.payment.dto.request.WalletRequestDTO;
import com.ridebooking.payment.dto.response.WalletResponseDTO;
import com.ridebooking.payment.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/add-money")
    public ResponseEntity<ApiResponse<WalletResponseDTO>> addMoney(
            @Valid @RequestBody WalletRequestDTO requestDTO) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Money added successfully",
                        walletService.addMoney(requestDTO)
                )
        );
    }

    @PostMapping("/deduct-money")
    public ResponseEntity<ApiResponse<WalletResponseDTO>> deductMoney(
            @Valid @RequestBody WalletRequestDTO requestDTO) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Money deducted successfully",
                        walletService.deductMoney(requestDTO)
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<WalletResponseDTO>> getWallet(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wallet fetched successfully",
                        walletService.getWalletByUserId(userId)
                )
        );
    }
}
