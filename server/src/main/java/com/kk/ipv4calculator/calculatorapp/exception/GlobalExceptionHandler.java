package com.kk.ipv4calculator.calculatorapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidMaskException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMaskException(InvalidMaskException ex){
        ErrorResponse errorResponse = new ErrorResponse("414", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAddressException(InvalidAddressException ex){
        ErrorResponse errorResponse = new ErrorResponse("415", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSubnetsAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSubnetsAmountException(InvalidSubnetsAmountException ex){
        ErrorResponse errorResponse = new ErrorResponse("416", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException ex){
        ErrorResponse errorResponse = new ErrorResponse("413", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
