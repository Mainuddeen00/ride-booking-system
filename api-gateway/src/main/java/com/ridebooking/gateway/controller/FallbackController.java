package com.ridebooking.gateway.controller;

import com.ridebooking.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/{service}")
    public Mono<ApiResponse<Void>> fallback(@PathVariable String service) {
        log.warn("Fallback triggered for service: {}", service);

        String message = String.format("Service '%s' is currently unavailable. Please try again later.", service);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path("/fallback/" + service)
                .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();

        return Mono.just(response);
    }
}