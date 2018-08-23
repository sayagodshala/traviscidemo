package com.merabreak.pushNotifications.events;

import com.merabreak.models.PushDetails;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vinay on 11/10/2016.
 */
public class PushType {

    @SerializedName("flag")
    String type;

    @SerializedName("msg")
    String message;

    @SerializedName("details")
    ArrayList<PushDetails> details;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PushDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<PushDetails> details) {
        this.details = details;
    }
}
