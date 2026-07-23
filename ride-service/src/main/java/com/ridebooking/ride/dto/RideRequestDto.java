package com.ridebooking.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RideRequestDto {

    @NotNull(message = "riderId is required")
    private Long riderId;

    @NotBlank(message = "pickupLocation is required")
    private String pickupLocation;

    @NotBlank(message = "dropLocation is required")
    private String dropLocation;

    @NotNull
    private Double pickupLatitude;

    @NotNull
    private Double pickupLongitude;

    @NotNull
    private Double dropLatitude;

    @NotNull
    private Double dropLongitude;
}
