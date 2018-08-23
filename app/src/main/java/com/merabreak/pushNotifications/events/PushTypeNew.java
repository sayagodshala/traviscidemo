package com.merabreak.pushNotifications.events;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ETPL-002 on 11-Jul-17.
 */

public class PushTypeNew {

    @SerializedName("flag")
    String type;

    String slug;
    String points;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
