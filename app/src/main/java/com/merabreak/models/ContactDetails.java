package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vinay on 11/17/2016.
 */
public class ContactDetails implements Parcelable{

    public String contactName;
    public String contactNum;
    public boolean selected;

    public ContactDetails() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactName);
        dest.writeString(this.contactNum);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected ContactDetails(Parcel in) {
        this.contactName = in.readString();
        this.contactNum = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<ContactDetails> CREATOR = new Creator<ContactDetails>() {
        @Override
        public ContactDetails createFromParcel(Parcel source) {
            return new ContactDetails(source);
        }

        @Override
        public ContactDetails[] newArray(int size) {
            return new ContactDetails[size];
        }
    };

}
