package com.example.UserAuth.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private LocalDateTime dateTime;
    private String message;

    public ApiError(int httpStatusCode, HttpStatus httpStatus, LocalDateTime dateTime, String message) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.dateTime = dateTime;
        this.message = message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}
