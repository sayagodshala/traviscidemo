package com.merabreak.models.facebook;

import com.google.gson.annotations.SerializedName;

public class Cover {

    private String id;

    @SerializedName("offset_y")
    private int offsetY;

    private String source;

    public Cover() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
