package com.merabreak;

/**
 * Created by Saya Godshala on 1/19/2016.
 */
public class ForgotPasswordEvent {

    private String param;

    public ForgotPasswordEvent(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
