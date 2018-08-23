package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 21-Nov-17.
 */

public class Points implements Parcelable {

    String norPoints;
    String supPoints;

    public Points(){

    }

    public Points(String norPoints, String supPoints) {
        this.norPoints = norPoints;
        this.supPoints = supPoints;
    }

    public String getNorPoints() {
        return norPoints;
    }

    public void setNorPoints(String norPoints) {
        this.norPoints = norPoints;
    }

    public String getSupPoints() {
        return supPoints;
    }

    public void setSupPoints(String supPoints) {
        this.supPoints = supPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(norPoints);
        parcel.writeString(supPoints);
    }
    public static final Parcelable.Creator<Points> CREATOR = new Parcelable.Creator<Points>() {
        public Points createFromParcel(Parcel in) {
            return new Points(in);
        }
        public Points[] newArray(int size) {
            return new Points[size];
        }
    };
    private Points(Parcel in){
        norPoints = in.readString();
        supPoints = in.readString();
    }
}
