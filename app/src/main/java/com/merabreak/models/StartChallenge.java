package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 9/26/2016.
 */
public class StartChallenge implements Parcelable {

     String playedId;

    public StartChallenge() {
    }

    public StartChallenge(String playedId) {
        this.playedId = playedId;
    }

    public String getPlayedId() {
        return playedId;
    }

    public void setPlayedId(String playedId) {
        this.playedId = playedId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(playedId);
    }

    public static final Parcelable.Creator<StartChallenge> CREATOR = new Parcelable.Creator<StartChallenge>() {
        public StartChallenge createFromParcel(Parcel in) {
            return new StartChallenge(in);
        }
        public StartChallenge[] newArray(int size) {
            return new StartChallenge[size];
        }
    };
    private StartChallenge(Parcel in){
        playedId = in.readString();

    }
}
