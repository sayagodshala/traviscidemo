package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.merabreak.models.challenge.Challenge;

import java.util.List;

/**
 * Created by sayagodshala on 27/07/16.
 */
public class HomeCategory implements Parcelable {

    private String name;
    private String tabName;
    private List<Challenge> featuredChallenge;
    private List<Challenge> challenges;

    public HomeCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Challenge> getFeaturedChallenge() {
        return featuredChallenge;
    }

    public void setFeaturedChallenge(List<Challenge> featuredChallenge) {
        this.featuredChallenge = featuredChallenge;
    }

    /* public Challenge getFeaturedChallenge() {
        return featuredChallenge;
    }

    public void setFeaturedChallenge(Challenge featuredChallenge) {
        this.featuredChallenge = featuredChallenge;
    }*/

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
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
        dest.writeString(this.name);
        dest.writeString(this.tabName);
       // dest.writeParcelable(this.featuredChallenge, flags);
        dest.writeTypedList(this.featuredChallenge);
        dest.writeTypedList(this.challenges);
    }

    protected HomeCategory(Parcel in) {
        this.name = in.readString();
        this.tabName = in.readString();
       // this.featuredChallenge = in.readParcelable(Challenge.class.getClassLoader());
        this.featuredChallenge = in.createTypedArrayList(Challenge.CREATOR);
        this.challenges = in.createTypedArrayList(Challenge.CREATOR);
    }

    public static final Parcelable.Creator<HomeCategory> CREATOR = new Parcelable.Creator<HomeCategory>() {
        @Override
        public HomeCategory createFromParcel(Parcel source) {
            return new HomeCategory(source);
        }

        @Override
        public HomeCategory[] newArray(int size) {
            return new HomeCategory[size];
        }
    };
}
