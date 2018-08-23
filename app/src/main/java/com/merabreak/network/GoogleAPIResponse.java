package com.merabreak.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayagodshala on 8/8/2015.
 */
public class GoogleAPIResponse<T> {
    private String status;
    private T predictions;
    @SerializedName("error_message")
    private String errorMessage;

    public GoogleAPIResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getPredictions() {
        return predictions;
    }

    public void setPredictions(T predictions) {
        this.predictions = predictions;
    }
}