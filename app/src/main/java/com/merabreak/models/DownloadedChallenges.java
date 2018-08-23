package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 10/17/2016.
 */
public class DownloadedChallenges implements Parcelable {

    private String slug;
    private int isDownloaded;

    public DownloadedChallenges() {
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

   /* public int getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(slug);
           // dest.writeInt(isDownloaded);
    }

    public static final Parcelable.Creator<DownloadedChallenges> CREATOR = new Parcelable.Creator<DownloadedChallenges>() {
        public DownloadedChallenges createFromParcel(Parcel in) {
            return new DownloadedChallenges(in);
        }
        public DownloadedChallenges[] newArray(int size) {
            return new DownloadedChallenges[size];
        }
    };
    private DownloadedChallenges(Parcel in){
        slug = in.readString();
       // isDownloaded = in.readInt();
    }
}
