package com.example.uniapi.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(int statusCode, String error, String message, ZonedDateTime timestamp, String path) { }
