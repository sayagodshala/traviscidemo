package com.merabreak.models.liveData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 09-Feb-18.
 */

public class EventScore implements Parcelable{

    private String event_id;
    private TeamOne team1;
    private TeamTwo team2;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public TeamOne getTeam1() {
        return team1;
    }

    public void setTeam1(TeamOne team1) {
        this.team1 = team1;
    }

    public TeamTwo getTeam2() {
        return team2;
    }

    public void setTeam2(TeamTwo team2) {
        this.team2 = team2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.team1 , flags);
        dest.writeParcelable(this.team2 , flags);
        dest.writeString(this.event_id);
    }

    protected EventScore(Parcel in) {
        this.team1 = in.readParcelable(TeamOne.class.getClassLoader());
        this.team2 = in.readParcelable(TeamTwo.class.getClassLoader());
        this.event_id = in.readString();
    }

    public static final Parcelable.Creator<EventScore> CREATOR = new Parcelable.Creator<EventScore>() {
        public EventScore createFromParcel(Parcel source) {
            return new EventScore(source);
        }

        public EventScore[] newArray(int size) {
            return new EventScore[size];
        }
    };
}
