package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 06-Dec-17.
 */

public class SpinWheelIndex implements Parcelable{

    private String coins;
    String text;
    int color;
    int icon;

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.coins);
        parcel.writeString(this.text);
        parcel.writeInt(this.color);
        parcel.writeInt(this.icon);
    }

    public static final Parcelable.Creator<SpinWheelIndex> CREATOR = new Creator<SpinWheelIndex>() {
        public SpinWheelIndex createFromParcel(Parcel in) {
            return new SpinWheelIndex(in);
        }
        public SpinWheelIndex[] newArray(int size) {
            return new SpinWheelIndex[size];
        }
    };
    public SpinWheelIndex(Parcel in){
        this.coins = in.readString();
        this.text = in.readString();
        this.color = in.readInt();
        this.icon = in.readInt();
    }
}
