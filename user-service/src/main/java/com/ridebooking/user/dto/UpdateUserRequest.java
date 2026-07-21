package com.ridebooking.user.dto;

import lombok.Data;

/**
 * Request DTO for updating a user profile.
 */
@Data
public class UpdateUserRequest {

    private String fullName;

    private String phone;

    private String profileImage;

    private Boolean active;
}