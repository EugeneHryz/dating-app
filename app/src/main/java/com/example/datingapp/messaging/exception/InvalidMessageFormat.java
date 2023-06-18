package com.example.datingapp.messaging.exception;

public class InvalidMessageFormat extends RuntimeException {

    public InvalidMessageFormat() {
        super();
    }

    public InvalidMessageFormat(String message) {
        super(message);
    }

    public InvalidMessageFormat(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMessageFormat(Throwable cause) {
        super(cause);
    }
}
