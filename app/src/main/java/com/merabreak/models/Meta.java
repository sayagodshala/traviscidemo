package com.merabreak.models;

/**
 * Created by Saya Godshala on 1/13/2016.
 */
public class Meta {
    public String message;
    public boolean status;
    public int statusCode;
    public String newAuthToken;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getNewAuthToken() {
        return newAuthToken;
    }

    public void setNewAuthToken(String newAuthToken) {
        this.newAuthToken = newAuthToken;
    }
}
