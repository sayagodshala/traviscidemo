package com.merabreak;

/**
 * Created by sayagodshala on 8/8/2015.
 */
public class OTPRecievedEvent {
    private String otp;

    public OTPRecievedEvent(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
