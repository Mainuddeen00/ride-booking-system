package com.ridebooking.payment.controller;

import com.ridebooking.payment.common.ApiResponse;
import com.ridebooking.payment.dto.response.InvoiceResponseDTO;
import com.ridebooking.payment.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/generate/{paymentId}")
    public ResponseEntity<ApiResponse<InvoiceResponseDTO>> generateInvoice(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Invoice generated successfully",
                        invoiceService.generateInvoice(paymentId)
                )
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<InvoiceResponseDTO>> getInvoice(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Invoice fetched successfully",
                        invoiceService.getInvoice(paymentId)
                )
        );
    }
}
