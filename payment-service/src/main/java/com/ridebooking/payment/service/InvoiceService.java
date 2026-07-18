package com.ridebooking.payment.service;

import com.ridebooking.payment.dto.response.InvoiceResponseDTO;

public interface InvoiceService {

    InvoiceResponseDTO generateInvoice(Long paymentId);

    InvoiceResponseDTO getInvoice(Long paymentId);

}
