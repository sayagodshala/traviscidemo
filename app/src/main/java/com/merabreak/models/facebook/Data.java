package com.merabreak.models.facebook;

import com.google.gson.annotations.SerializedName;

public class Data {
    private int height;
    @SerializedName("is_silhouette")
    private boolean silhouette;
    private String url;
    private int width;

    public Data() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isSilhouette() {
        return silhouette;
    }

    public void setSilhouette(boolean silhouette) {
        this.silhouette = silhouette;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
