package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 7/26/2016.
 */
public class Circles implements Parcelable {

    String code;
    String name;

    public Circles(){

    }

    public Circles(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(name);
    }
    public static final Parcelable.Creator<Circles> CREATOR = new Parcelable.Creator<Circles>() {
        public Circles createFromParcel(Parcel in) {
            return new Circles(in);
        }
        public Circles[] newArray(int size) {
            return new Circles[size];
        }
    };
    private Circles(Parcel in){

        code = in.readString();
        name = in.readString();
    }

}
