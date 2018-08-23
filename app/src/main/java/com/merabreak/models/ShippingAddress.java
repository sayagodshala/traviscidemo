package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sayagodshala on 03/06/16.
 */
public class ShippingAddress implements Parcelable {

    private String id;
    private String fullName;
    private String phone;
    private String alternatePhone;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zipCode;
    private int stateId;
    private int cityId;
    private boolean selected;


    public ShippingAddress() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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
        dest.writeString(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.phone);
        dest.writeString(this.alternatePhone);
        dest.writeString(this.addressLine1);
        dest.writeString(this.addressLine2);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.zipCode);
        dest.writeInt(this.stateId);
        dest.writeInt(this.cityId);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected ShippingAddress(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.phone = in.readString();
        this.alternatePhone = in.readString();
        this.addressLine1 = in.readString();
        this.addressLine2 = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.zipCode = in.readString();
        this.stateId = in.readInt();
        this.cityId = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<ShippingAddress> CREATOR = new Creator<ShippingAddress>() {
        @Override
        public ShippingAddress createFromParcel(Parcel source) {
            return new ShippingAddress(source);
        }

        @Override
        public ShippingAddress[] newArray(int size) {
            return new ShippingAddress[size];
        }
    };
}
