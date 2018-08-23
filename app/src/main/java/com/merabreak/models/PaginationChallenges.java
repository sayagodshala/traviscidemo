package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.merabreak.models.challenge.Challenge;

import java.util.List;

/**
 * Created by Ewebcore on 8/17/2016.
 */
public class PaginationChallenges implements Parcelable {

    private String count;
    private String pages;
    private String current_page;
    private String nexturl;
    private String prevurl;
    private List<Challenge> challenges;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getNexturl() {
        return nexturl;
    }

    public void setNexturl(String nexturl) {
        this.nexturl = nexturl;
    }

    public String getPrevurl() {
        return prevurl;
    }

    public void setPrevurl(String prevurl) {
        this.prevurl = prevurl;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }



    public PaginationChallenges() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.count);
        dest.writeString(this.pages);
        dest.writeString(this.current_page);
        dest.writeString(this.nexturl);
        dest.writeString(this.prevurl);
        dest.writeTypedList(this.challenges);
    }

    protected PaginationChallenges(Parcel in) {
        this.count = in.readString();
        this.pages = in.readString();
        this.current_page = in.readString();
        this.nexturl = in.readString();
        this.prevurl = in.readString();
        this.challenges = in.createTypedArrayList(Challenge.CREATOR);

    }

    public static final Creator<PaginationChallenges> CREATOR = new Creator<PaginationChallenges>() {
        @Override
        public PaginationChallenges createFromParcel(Parcel in) {
            return new PaginationChallenges(in);
        }

        @Override
        public PaginationChallenges[] newArray(int size) {
            return new PaginationChallenges[size];
        }
    };




 /*   public static Creator<PaginationChallenges> getCREATOR() {
        return CREATOR;
    }

*/








}
