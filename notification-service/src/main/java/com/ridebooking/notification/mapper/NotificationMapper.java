package com.ridebooking.notification.mapper;

import com.ridebooking.notification.dto.NotificationRequestDTO;
import com.ridebooking.notification.dto.NotificationResponseDTO;
import com.ridebooking.notification.entity.Notification;
import com.ridebooking.notification.enums.NotificationStatus;

import java.time.LocalDateTime;

public class NotificationMapper {

    private NotificationMapper() {
    }

    public static Notification toEntity(NotificationRequestDTO dto) {

        return Notification.builder()
                .userId(dto.getUserId())
                .rideId(dto.getRideId())
                .paymentId(dto.getPaymentId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NotificationResponseDTO toResponseDTO(Notification notification) {

        return NotificationResponseDTO.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .rideId(notification.getRideId())
                .paymentId(notification.getPaymentId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .build();
    }
}