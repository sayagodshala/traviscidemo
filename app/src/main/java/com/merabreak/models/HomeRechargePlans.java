package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Vinay on 8/10/2016.
 */
public class HomeRechargePlans implements Parcelable {

        private String name;
        private List<RechargePlans> plans;

    public HomeRechargePlans() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RechargePlans> getPlans() {
        return plans;
    }

    public void setPlans(List<RechargePlans> plans) {
        this.plans = plans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(this.plans);
    }

    protected HomeRechargePlans(Parcel in) {
        this.name = in.readString();
        this.plans = in.createTypedArrayList(RechargePlans.CREATOR);
    }

    public static final Parcelable.Creator<HomeRechargePlans> CREATOR = new Parcelable.Creator<HomeRechargePlans>() {
        @Override
        public HomeRechargePlans createFromParcel(Parcel source) {
            return new HomeRechargePlans(source);
        }

        @Override
        public HomeRechargePlans[] newArray(int size) {
            return new HomeRechargePlans[size];
        }
    };
}
