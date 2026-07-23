package com.ridebooking.notification.exception;

public class NotificationAlreadySentException extends RuntimeException {

    public NotificationAlreadySentException(String message) {
        super(message);
    }

}