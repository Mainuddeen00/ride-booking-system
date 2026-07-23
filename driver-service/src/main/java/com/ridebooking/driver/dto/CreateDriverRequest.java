package com.ridebooking.driver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDriverRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String vehicleNumber;

    @NotBlank
    private String vehicleType;

    @NotBlank
    private String vehicleModel;

    @NotBlank
    private String vehicleColor;

    @NotBlank
    private String registrationNumber;
}