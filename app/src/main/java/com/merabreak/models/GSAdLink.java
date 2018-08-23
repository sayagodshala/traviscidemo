package com.merabreak.models;

import com.google.gson.annotations.SerializedName;

public class GSAdLink {

    @SerializedName("web_link")
    private String GSAdLink;

    private String GSAdLinkName;

    public String getGSAdLink() {
        return GSAdLink;
    }

    public String getGSAdLinkName() {
        return GSAdLinkName;
    }

    public void setGSAdLinkName(String GSAdLinkName) {
        this.GSAdLinkName = GSAdLinkName;
    }

    public void setGSAdLink(String GSAdLink) {
        this.GSAdLink = GSAdLink;
    }
}
