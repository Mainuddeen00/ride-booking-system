package com.ridebooking.ride.dto;

import lombok.Data;

@Data
public class DriverDto {
    private Long id;
    private String name;
    private String status;
    private Double currentLatitude;
    private Double currentLongitude;
}
