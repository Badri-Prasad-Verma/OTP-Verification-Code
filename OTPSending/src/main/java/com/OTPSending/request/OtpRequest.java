package com.OTPSending.request;

public class OtpRequest {

    private String phoneNumber;

    // Constructors, getters, and setters

    public OtpRequest() {
        // Default constructor
    }

    public OtpRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

