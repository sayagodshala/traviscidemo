package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 7/26/2016.
 */
public class Operators implements Parcelable{

    String code;
    String name;

    public Operators() {
    }

    public Operators(String code, String name) {
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

    public static final Parcelable.Creator<Operators> CREATOR = new Parcelable.Creator<Operators>() {
        public Operators createFromParcel(Parcel in) {
            return new Operators(in);
        }
        public Operators[] newArray(int size) {
            return new Operators[size];
        }
    };
    private Operators(Parcel in){

        code = in.readString();
        name = in.readString();
    }


}
