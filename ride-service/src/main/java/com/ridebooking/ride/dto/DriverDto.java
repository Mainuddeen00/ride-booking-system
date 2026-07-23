package com.ridebooking.ride.dto;

import lombok.Data;

@Data
public class DriverDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String licenseNumber;
    private String availabilityStatus;
    private Double rating;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleColor;
    private String registrationNumber;
    // Add location fields
    private Double currentLatitude;
    private Double currentLongitude;
}
