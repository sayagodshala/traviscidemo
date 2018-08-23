package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 10/27/2016.
 */
public class Memimages implements Parcelable {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Memimages() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
    }

    protected Memimages(Parcel in) {
        this.url = in.readString();
    }

    public static final Creator<Memimages> CREATOR = new Creator<Memimages>() {
        public Memimages createFromParcel(Parcel source) {
            return new Memimages(source);
        }

        public Memimages[] newArray(int size) {
            return new Memimages[size];
        }
    };
}
