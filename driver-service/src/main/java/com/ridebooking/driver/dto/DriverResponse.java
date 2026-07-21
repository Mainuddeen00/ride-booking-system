package com.ridebooking.driver.dto;

import com.ridebooking.driver.entity.AvailabilityStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String licenseNumber;

    private AvailabilityStatus availabilityStatus;

    private Double rating;

    private String vehicleNumber;

    private String vehicleType;

    private String vehicleModel;

    private String vehicleColor;

    private String registrationNumber;
}