package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 31-Jul-17.
 */

public class StripParams implements Parcelable {

    private String stripImage;
    private int stripFlag;

    public StripParams() {
    }

    public String getStripImage() {
        return stripImage;
    }

    public void setStripImage(String stripImage) {
        this.stripImage = stripImage;
    }

    public int getStripFlag() {
        return stripFlag;
    }

    public void setStripFlag(int stripFlag) {
        this.stripFlag = stripFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(stripImage);
        parcel.writeInt(stripFlag);
    }

    public static final Parcelable.Creator<StripParams> CREATOR = new Parcelable.Creator<StripParams>() {
        public StripParams createFromParcel(Parcel in) {
            return new StripParams(in);
        }
        public StripParams[] newArray(int size) {
            return new StripParams[size];
        }
    };
    private StripParams(Parcel in){

        stripImage = in.readString();
        stripFlag = in.readInt();
    }
}
