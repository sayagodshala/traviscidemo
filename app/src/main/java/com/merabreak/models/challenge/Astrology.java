package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Astrology implements Parcelable {

    private int id;
    @SerializedName("sign_name")
    private String signName;
    private String title;
    private String detailsTitle;
    @SerializedName("sign_date")
    private String signDate;
    @SerializedName("cover_image")
    private String coverImage;
    @SerializedName("bg_image")
    private String backgroundImage;
    @SerializedName("ad_image")
    private String adImage;
    @SerializedName("ad_id")
    private int adId;
    @SerializedName("astrologyContent")
    private List<Article> articles = new ArrayList<Article>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailsTitle() {
        return detailsTitle;
    }

    public void setDetailsTitle(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.signName);
        dest.writeString(this.signDate);
        dest.writeString(this.detailsTitle);
        dest.writeString(this.coverImage);
        dest.writeString(this.backgroundImage);
        dest.writeString(this.adImage);
        dest.writeInt(this.adId);
        dest.writeTypedList(this.articles);
    }

    protected Astrology(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.signName = in.readString();
        this.signDate = in.readString();
        this.detailsTitle = in.readString();
        this.coverImage = in.readString();
        this.backgroundImage = in.readString();
        this.adImage = in.readString();
        this.adId = in.readInt();
        this.articles = in.createTypedArrayList(Article.CREATOR);
    }

        public static final Creator<Astrology> CREATOR = new Creator<Astrology>() {
            @Override
            public Astrology createFromParcel(Parcel source) {
                return new Astrology(source);
            }

            @Override
            public Astrology[] newArray(int size) {
                return new Astrology[size];
            }
        };

}
