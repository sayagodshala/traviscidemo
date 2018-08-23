package com.merabreak.models.liveData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 09-Feb-18.
 */

public class TeamTwo implements Parcelable{

    private String fullname;
    private String total_score;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullname);
        dest.writeString(this.total_score);
    }

    protected TeamTwo(Parcel in) {
        this.fullname = in.readString();
        this.total_score = in.readString();
    }

    public static final Parcelable.Creator<TeamTwo> CREATOR = new Parcelable.Creator<TeamTwo>() {
        public TeamTwo createFromParcel(Parcel source) {
            return new TeamTwo(source);
        }

        public TeamTwo[] newArray(int size) {
            return new TeamTwo[size];
        }
    };
}
