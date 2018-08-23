package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 8/31/2016.
 */
public class Winner implements Parcelable{

    private String winnerImage;
    private String winnerName;

    public Winner() {
    }

    public String getWinnerImage() {
        return winnerImage;
    }

    public void setWinnerImage(String winnerImage) {
        this.winnerImage = winnerImage;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.winnerImage);
            dest.writeString(this.winnerName);
    }

    protected Winner(Parcel in) {
        this.winnerImage = in.readString();
        this.winnerName = in.readString();
    }

    public static final Creator<Winner> CREATOR = new Creator<Winner>() {
        public Winner createFromParcel(Parcel source) {
            return new Winner(source);
        }

        public Winner[] newArray(int size) {
            return new Winner[size];
        }
    };
}
