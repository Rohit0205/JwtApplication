package com.roh.jwtApplication.CustomExceptionHandler;

import com.roh.jwtApplication.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<ErrorResponseDto> handleOtpException(OtpException ex) {

        ErrorResponseDto errorResponse =
                new ErrorResponseDto(
                        ex.getStatus().value(),
                        ex.getMessage()
                );

        return ResponseEntity
                .status(ex.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenException(
            RefreshTokenException ex) {

        ErrorResponseDto errorResponse =
                new ErrorResponseDto(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage()
                );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
