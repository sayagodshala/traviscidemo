package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Vinay on 11/24/2016.
 */
public class MainContactDetails implements Parcelable{

    private List<ContactDetails> contactDetails;

    public MainContactDetails() {
    }

    public List<ContactDetails> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<ContactDetails> contactDetails) {
        this.contactDetails = contactDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.contactDetails);
    }

    protected MainContactDetails(Parcel in) {
        this.contactDetails = in.createTypedArrayList(ContactDetails.CREATOR);
    }

    public static final Creator<MainContactDetails> CREATOR = new Creator<MainContactDetails>() {
        @Override
        public MainContactDetails createFromParcel(Parcel source) {
            return new MainContactDetails(source);
        }

        @Override
        public MainContactDetails[] newArray(int size) {
            return new MainContactDetails[size];
        }
    };
}
