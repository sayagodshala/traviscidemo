package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ETPL-002 on 06-Jan-18.
 */

public class TermsAndConditions implements Parcelable {

    private String termField;

    public TermsAndConditions() {
    }

    public String getTermField() {
        return termField;
    }

    public void setTermField(String termField) {
        this.termField = termField;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.termField);
    }

    protected TermsAndConditions(Parcel in) {
        this.termField = in.readString();
    }

    public static final Parcelable.Creator<TermsAndConditions> CREATOR = new Parcelable.Creator<TermsAndConditions>() {
        @Override
        public TermsAndConditions createFromParcel(Parcel source) {
            return new TermsAndConditions(source);
        }

        @Override
        public TermsAndConditions[] newArray(int size) {
            return new TermsAndConditions[size];
        }
    };
}
