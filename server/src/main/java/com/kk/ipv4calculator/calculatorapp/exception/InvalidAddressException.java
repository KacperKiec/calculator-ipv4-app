package com.kk.ipv4calculator.calculatorapp.exception;

public class InvalidAddressException extends IllegalArgumentException{
    public InvalidAddressException(String msg) {
        super(msg);
    }
}
