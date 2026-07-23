package com.ridebooking.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    @NotNull
    private Long userId;

    private Long rideId;

    private Long paymentId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

}