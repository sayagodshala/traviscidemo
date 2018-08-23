package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sayagodshala on 16/03/16.
 */
public class Coin implements Parcelable {

    private String event;
    private String action;
    private String coins;
    private String balance;
    private String eventDate;
    private String regRefName;
    private String regRefMobile;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getRegRefName() {
        return regRefName;
    }

    public void setRegRefName(String regRefName) {
        this.regRefName = regRefName;
    }

    public String getRegRefMobile() {
        return regRefMobile;
    }

    public void setRegRefMobile(String regRefMobile) {
        this.regRefMobile = regRefMobile;
    }

    public Coin() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.action);
        dest.writeString(this.coins);
        dest.writeString(this.balance);
        dest.writeString(this.eventDate);
        dest.writeString(this.regRefName);
        dest.writeString(this.regRefMobile);
    }

    protected Coin(Parcel in) {
        this.event = in.readString();
        this.action = in.readString();
        this.coins = in.readString();
        this.balance = in.readString();
        this.eventDate = in.readString();
        this.regRefName = in.readString();
        this.regRefMobile = in.readString();
    }

    public static final Parcelable.Creator<Coin> CREATOR = new Parcelable.Creator<Coin>() {
        public Coin createFromParcel(Parcel source) {
            return new Coin(source);
        }

        public Coin[] newArray(int size) {
            return new Coin[size];
        }
    };
}


