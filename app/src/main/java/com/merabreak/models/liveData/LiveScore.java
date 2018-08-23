package com.merabreak.models.liveData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 09-Feb-18.
 */

public class LiveScore implements Parcelable{

    private NewValue new_val;

    public NewValue getNew_val() {
        return new_val;
    }

    public void setNew_val(NewValue new_val) {
        this.new_val = new_val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.new_val , flags);
    }

    protected LiveScore(Parcel in) {
        this.new_val = in.readParcelable(NewValue.class.getClassLoader());
    }

    public static final Parcelable.Creator<LiveScore> CREATOR = new Parcelable.Creator<LiveScore>() {
        public LiveScore createFromParcel(Parcel source) {
            return new LiveScore(source);
        }

        public LiveScore[] newArray(int size) {
            return new LiveScore[size];
        }
    };
}
