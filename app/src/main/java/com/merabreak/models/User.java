package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saya Godshala on 1/13/2016.
 */

public class User implements Parcelable {

    private String fullName;
    private String email;
    private String about;
    private String dob;
    private String gender;
    private String phone;
    private String image;
    private String state;
    private String city;
    private String zipCode;
    private String accessToken;
    private String authToken;
    private String accountType;
    private String userRank;
    private int dataEnabled;
    private String selectedCity;
    private int userId;
    private int firstOpened;
    private int referOpened;
    private String location;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public int getDataEnabled() {
        return dataEnabled;
    }

    public void setDataEnabled(int dataEnabled) {
        this.dataEnabled = dataEnabled;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFirstOpened() {
        return firstOpened;
    }

    public void setFirstOpened(int firstOpened) {
        this.firstOpened = firstOpened;
    }

    public int getReferOpened() {
        return referOpened;
    }

    public void setReferOpened(int referOpened) {
        this.referOpened = referOpened;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.about);
        dest.writeString(this.dob);
        dest.writeString(this.gender);
        dest.writeString(this.phone);
        dest.writeString(this.image);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.zipCode);
        dest.writeString(this.accessToken);
        dest.writeString(this.authToken);
        dest.writeString(this.accountType);
        dest.writeString(this.userRank);
        dest.writeInt(this.dataEnabled);
        dest.writeString(this.selectedCity);
        dest.writeInt(this.userId);
        dest.writeInt(this.firstOpened);
        dest.writeInt(this.referOpened);
        dest.writeString(this.location);
    }

    protected User(Parcel in) {
        this.fullName = in.readString();
        this.email = in.readString();
        this.about = in.readString();
        this.dob = in.readString();
        this.gender = in.readString();
        this.phone = in.readString();
        this.image = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.zipCode = in.readString();
        this.accessToken = in.readString();
        this.authToken = in.readString();
        this.accountType = in.readString();
        this.userRank = in.readString();
        this.dataEnabled = in.readInt();
        this.selectedCity = in.readString();
        this.userId = in.readInt();
        this.firstOpened = in.readInt();
        this.referOpened = in.readInt();
        this.location = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
