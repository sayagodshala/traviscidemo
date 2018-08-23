package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.merabreak.models.challenge.Astrology;
import com.merabreak.models.challenge.Challenge;

import java.util.List;

public class GaneshaSpeaks implements Parcelable{

    private String name;
    private String tabName;
    @SerializedName("astro")
    private List<Astrology> astrology;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<Astrology> getAstrology() {
        return astrology;
    }

    public void setAstrology(List<Astrology> astrology) {
        this.astrology = astrology;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.tabName);
        dest.writeTypedList(this.astrology);
    }

    protected GaneshaSpeaks(Parcel in) {
        this.name = in.readString();
        this.tabName = in.readString();
        this.astrology = in.createTypedArrayList(Astrology.CREATOR);
    }

    public static final Parcelable.Creator<GaneshaSpeaks> CREATOR = new Parcelable.Creator<GaneshaSpeaks>() {
        @Override
        public GaneshaSpeaks createFromParcel(Parcel source) {
            return new GaneshaSpeaks(source);
        }

        @Override
        public GaneshaSpeaks[] newArray(int size) {
            return new GaneshaSpeaks[size];
        }
    };


}
