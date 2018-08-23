package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 12/13/2016.
 */
public class Cities implements Parcelable {

    private String cityName;
    private String cityId;
    private String cityImage;
    private int citySelection;

    public Cities() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityImage() {
        return cityImage;
    }

    public void setCityImage(String cityImage) {
        this.cityImage = cityImage;
    }

    public int getCitySelection() {
        return citySelection;
    }

    public void setCitySelection(int citySelection) {
        this.citySelection = citySelection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityName);
        dest.writeString(this.cityId);
        dest.writeString(this.cityImage);
        dest.writeInt(this.citySelection);
    }

    protected Cities(Parcel in) {
        this.cityName = in.readString();
        this.cityId = in.readString();
        this.cityImage = in.readString();
        this.citySelection = in.readInt();
    }

    public static final Parcelable.Creator<Cities> CREATOR = new Parcelable.Creator<Cities>() {
        public Cities createFromParcel(Parcel source) {
            return new Cities(source);
        }

        public Cities[] newArray(int size) {
            return new Cities[size];
        }
    };
}
