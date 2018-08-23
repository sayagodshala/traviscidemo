package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.merabreak.models.challenge.Challenge;

import java.util.List;

/**
 * Created by ETPL-002 on 04-Dec-17.
 */

public class SpinWheelDetails implements Parcelable{

    private int spin_id;
    private int target_index;

    @SerializedName("all_spin")
    private List<SpinWheelIndex> spinDetails;

    public SpinWheelDetails() {

    }

    public int getSpin_id() {
        return spin_id;
    }

    public void setSpin_id(int spin_id) {
        this.spin_id = spin_id;
    }

    public int getTarget_index() {
        return target_index;
    }

    public void setTarget_index(int target_index) {
        this.target_index = target_index;
    }

    public List<SpinWheelIndex> getSpinDetails() {
        return spinDetails;
    }

    public void setSpinDetails(List<SpinWheelIndex> spinDetails) {
        this.spinDetails = spinDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.spin_id);
        parcel.writeInt(this.target_index);
        parcel.writeTypedList(this.spinDetails);
    }

    public static final Parcelable.Creator<SpinWheelDetails> CREATOR = new Parcelable.Creator<SpinWheelDetails>() {
        public SpinWheelDetails createFromParcel(Parcel in) {
            return new SpinWheelDetails(in);
        }
        public SpinWheelDetails[] newArray(int size) {
            return new SpinWheelDetails[size];
        }
    };
    private SpinWheelDetails(Parcel in){
        this.spin_id = in.readInt();
        this.target_index = in.readInt();
        this.spinDetails = in.createTypedArrayList(SpinWheelIndex.CREATOR);
    }
}
