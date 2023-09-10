package com.example.final_UI_dev.entity;

public class otpverify {
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String otp;
    private String email;

    public otpverify(String otp, String email) {
        this.otp = otp;
        this.email = email;
    }
    public otpverify(){

    }
}
