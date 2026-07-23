package com.ridebooking.ride.exception;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(Long id) {
        super("Ride not found with id: " + id);
    }
}