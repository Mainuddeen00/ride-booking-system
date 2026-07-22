package com.ridebooking.notification.dto;

import com.ridebooking.notification.enums.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    private Long notificationId;

    private Long userId;

    private Long rideId;

    private Long paymentId;

    private String title;

    private String message;

    private NotificationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

}