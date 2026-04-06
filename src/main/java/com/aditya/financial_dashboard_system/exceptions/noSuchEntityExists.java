package com.aditya.financial_dashboard_system.exceptions;

public class noSuchEntityExists extends RuntimeException{
    private String message;

    public noSuchEntityExists(String message) {
        super(message);
        this.message = message;

    }
}
