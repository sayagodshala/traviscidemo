package com.merabreak.models.liveData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 09-Feb-18.
 */

public class NewValue implements Parcelable{

    private String sport;
    private EventScore event_score;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public EventScore getEvent_score() {
        return event_score;
    }

    public void setEvent_score(EventScore event_score) {
        this.event_score = event_score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.event_score , flags);
        dest.writeString(this.sport);
    }

    protected NewValue(Parcel in) {
        this.event_score = in.readParcelable(EventScore.class.getClassLoader());
        this.sport = in.readString();
    }

    public static final Parcelable.Creator<NewValue> CREATOR = new Parcelable.Creator<NewValue>() {
        public NewValue createFromParcel(Parcel source) {
            return new NewValue(source);
        }

        public NewValue[] newArray(int size) {
            return new NewValue[size];
        }
    };
}
