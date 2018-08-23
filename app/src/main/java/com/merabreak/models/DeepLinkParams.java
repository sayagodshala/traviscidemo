package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vinay on 12/22/2016.
 */
public class DeepLinkParams implements Parcelable {


    @SerializedName("+is_first_session")
    boolean isFirstSession;

    @SerializedName("+clicked_branch_link")
    boolean isClickedBranchLink;

    @SerializedName("$marketing_title")
    String marketingTitle;

    @SerializedName("~feature")
    String feature;

    @SerializedName("~marketing")
    String marketing;

    @SerializedName("+click_timestamp")
    long clickTimestamp;

    @SerializedName("+match_guaranteed")
    boolean matchGuaranteed;

    @SerializedName("~referring_link")
    String referringLink;

    @SerializedName("action")
    String action;

    @SerializedName("slug")
    String slug;

    public DeepLinkParams() {
    }

    public boolean isFirstSession() {
        return isFirstSession;
    }

    public void setIsFirstSession(boolean isFirstSession) {
        this.isFirstSession = isFirstSession;
    }

    public boolean isClickedBranchLink() {
        return isClickedBranchLink;
    }

    public void setIsClickedBranchLink(boolean isClickedBranchLink) {
        this.isClickedBranchLink = isClickedBranchLink;
    }

    public String getMarketingTitle() {
        return marketingTitle;
    }

    public void setMarketingTitle(String marketingTitle) {
        this.marketingTitle = marketingTitle;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }


    public String getMarketing() {
        return marketing;
    }

    public void setMarketing(String marketing) {
        this.marketing = marketing;
    }

    public long getClickTimestamp() {
        return clickTimestamp;
    }

    public void setClickTimestamp(long clickTimestamp) {
        this.clickTimestamp = clickTimestamp;
    }

    public boolean isMatchGuaranteed() {
        return matchGuaranteed;
    }

    public void setMatchGuaranteed(boolean matchGuaranteed) {
        this.matchGuaranteed = matchGuaranteed;
    }

    public String getReferringLink() {
        return referringLink;
    }

    public void setReferringLink(String referringLink) {
        this.referringLink = referringLink;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isFirstSession ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isClickedBranchLink ? (byte) 1 : (byte) 0);
        dest.writeString(this.marketingTitle);
        dest.writeString(this.feature);
        dest.writeString(this.marketing);
        dest.writeLong(this.clickTimestamp);
        dest.writeByte(this.matchGuaranteed ? (byte) 1 : (byte) 0);
        dest.writeString(this.referringLink);
        dest.writeString(this.action);
        dest.writeString(this.slug);
    }

    protected DeepLinkParams(Parcel in) {
        this.isFirstSession = in.readByte() != 0;
        this.isClickedBranchLink = in.readByte() != 0;
        this.marketingTitle = in.readString();
        this.feature = in.readString();
        this.marketing = in.readString();
        this.clickTimestamp = in.readLong();
        this.matchGuaranteed = in.readByte() != 0;
        this.referringLink = in.readString();
        this.action = in.readString();
        this.slug = in.readString();
    }

    public static final Parcelable.Creator<DeepLinkParams> CREATOR = new Parcelable.Creator<DeepLinkParams>() {
        @Override
        public DeepLinkParams createFromParcel(Parcel source) {
            return new DeepLinkParams(source);
        }

        @Override
        public DeepLinkParams[] newArray(int size) {
            return new DeepLinkParams[size];
        }
    };
}
