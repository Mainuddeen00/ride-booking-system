package com.ridebooking.notification.controller;

import com.ridebooking.notification.common.ApiResponse;
import com.ridebooking.notification.dto.NotificationRequestDTO;
import com.ridebooking.notification.dto.NotificationResponseDTO;
import com.ridebooking.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) {

        NotificationResponseDTO response =
                notificationService.createNotification(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> getNotificationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification fetched successfully",
                        notificationService.getNotificationById(id)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getNotificationsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notifications fetched successfully",
                        notificationService.getNotificationsByUserId(userId)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getAllNotifications() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notifications fetched successfully",
                        notificationService.getAllNotifications()
                )
        );
    }

    @PutMapping("/{id}/send")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> sendNotification(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification sent successfully",
                        notificationService.sendNotification(id)
                )
        );
    }
}