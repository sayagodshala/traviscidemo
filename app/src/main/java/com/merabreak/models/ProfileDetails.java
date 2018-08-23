package com.merabreak.models;

/**
 * Created by sayagodshala on 19/05/16.
 */
public class ProfileDetails {

    private int genderFlag;
    private int dobFlag;
    private int locationFlag;
    private int profilePercentage;


    public ProfileDetails() {

    }

    public int getGenderFlag() {
        return genderFlag;
    }

    public void setGenderFlag(int genderFlag) {
        this.genderFlag = genderFlag;
    }

    public int getDobFlag() {
        return dobFlag;
    }

    public void setDobFlag(int dobFlag) {
        this.dobFlag = dobFlag;
    }

    public int getLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(int locationFlag) {
        this.locationFlag = locationFlag;
    }

    public int getProfilePercentage() {
        return profilePercentage;
    }

    public void setProfilePercentage(int profilePercentage) {
        this.profilePercentage = profilePercentage;
    }
}
