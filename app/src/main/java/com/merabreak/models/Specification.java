package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sayagodshala on 01/06/16.
 */
public class Specification implements Parcelable {

    private String specField;
    private String specValue;

    public Specification() {
    }

    public String getSpecField() {
        return specField;
    }

    public void setSpecField(String specField) {
        this.specField = specField;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.specField);
        dest.writeString(this.specValue);
    }

    protected Specification(Parcel in) {
        this.specField = in.readString();
        this.specValue = in.readString();
    }

    public static final Parcelable.Creator<Specification> CREATOR = new Parcelable.Creator<Specification>() {
        @Override
        public Specification createFromParcel(Parcel source) {
            return new Specification(source);
        }

        @Override
        public Specification[] newArray(int size) {
            return new Specification[size];
        }
    };
}
