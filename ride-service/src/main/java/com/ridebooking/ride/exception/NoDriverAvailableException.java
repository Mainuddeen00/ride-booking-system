package com.ridebooking.ride.exception;

public class NoDriverAvailableException extends RuntimeException {
    public NoDriverAvailableException() {
        super("No drivers currently available");
    }
}
