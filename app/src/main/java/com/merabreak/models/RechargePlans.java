package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 8/10/2016.
 */
public class RechargePlans implements Parcelable{

    private String amount;
    private String validity;
    private String description;
    private String coins;
    private String tabName;

    public RechargePlans() {
    }

    public RechargePlans(String amount, String validity, String description, String coins) {
        this.amount = amount;
        this.validity = validity;
        this.description = description;
        this.coins = coins;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeString(this.validity);
        dest.writeString(this.description);
        dest.writeString(this.coins);
        dest.writeString(this.tabName);
    }

    protected RechargePlans(Parcel in){
        this.amount = in.readString();
        this.validity = in.readString();
        this.description = in.readString();
        this.coins = in.readString();
        this.tabName = in.readString();
    }

    public static final Creator<RechargePlans> CREATOR = new Creator<RechargePlans>() {

        @Override
        public RechargePlans createFromParcel(Parcel source) {
            return new RechargePlans(source);
        }

        @Override
        public RechargePlans[] newArray(int size) {
            return new RechargePlans[size];
        }
    };
}
