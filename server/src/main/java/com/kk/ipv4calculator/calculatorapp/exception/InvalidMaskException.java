package com.kk.ipv4calculator.calculatorapp.exception;

public class InvalidMaskException extends IllegalArgumentException {
    public InvalidMaskException(String msg) {
        super(msg);
    }
}
