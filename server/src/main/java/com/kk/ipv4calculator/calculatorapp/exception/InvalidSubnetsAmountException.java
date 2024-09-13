package com.kk.ipv4calculator.calculatorapp.exception;

public class InvalidSubnetsAmountException extends IllegalStateException{
    public InvalidSubnetsAmountException(String msg){
        super(msg);
    }
}
