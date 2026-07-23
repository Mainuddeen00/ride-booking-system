package com.ridebooking.ride.dto;

import com.ridebooking.ride.entity.RideStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RideResponseDto {
    private Long id;
    private Long riderId;
    private Long driverId;
    private String pickupLocation;
    private String dropLocation;
    private RideStatus status;
    private Double fare;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
}