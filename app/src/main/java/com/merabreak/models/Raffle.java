package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sayagodshala on 25/01/16.
 */
public class Raffle implements Parcelable {

    private String id;
    private String title;
    private String slug;
    private String description;
    private String image;
    private String detail_image;
    private String maxTickets;
    private String price;

    @SerializedName("coinsPerTicket")
    private String coinsPerTickets;
    private String endDateTime;
    private String productType;
    private String brandName;
    private boolean selected;
    private String remainingTicketLimit;
    private String totalRafflesPurchased;

    private List<Specification> specification;
    private List<TermsAndConditions> terms;

    public Raffle() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(String maxTickets) {
        this.maxTickets = maxTickets;
    }


    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    public String getCoinsPerTickets() {
        return coinsPerTickets;
    }

    public void setCoinsPerTickets(String coinsPerTickets) {
        this.coinsPerTickets = coinsPerTickets;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<Specification> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Specification> specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail_image() {
        return detail_image;
    }

    public void setDetail_image(String detail_image) {
        this.detail_image = detail_image;
    }

    public String getRemainingTicketLimit() {
        return remainingTicketLimit;
    }

    public void setRemainingTicketLimit(String remainingTicketLimit) {
        this.remainingTicketLimit = remainingTicketLimit;
    }

    public String getTotalRafflesPurchased() {
        return totalRafflesPurchased;
    }

    public void setTotalRafflesPurchased(String totalRafflesPurchased) {
        this.totalRafflesPurchased = totalRafflesPurchased;
    }

    public List<TermsAndConditions> getTerms() {
        return terms;
    }

    public void setTerms(List<TermsAndConditions> terms) {
        this.terms = terms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.slug);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.detail_image);
        dest.writeString(this.maxTickets);
        dest.writeString(this.coinsPerTickets);
        dest.writeString(this.endDateTime);
        dest.writeString(this.productType);
        dest.writeString(this.brandName);
        dest.writeString(this.price);
        dest.writeString(this.remainingTicketLimit);
        dest.writeString(this.totalRafflesPurchased);
        dest.writeTypedList(this.specification);
        dest.writeTypedList(this.terms);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected Raffle(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.slug = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.detail_image = in.readString();
        this.maxTickets = in.readString();
        this.coinsPerTickets = in.readString();
        this.endDateTime = in.readString();
        this.productType = in.readString();
        this.brandName = in.readString();
        this.price = in.readString();
        this.remainingTicketLimit = in.readString();
        this.totalRafflesPurchased = in.readString();
        this.selected = in.readByte() != 0;
        this.specification = in.createTypedArrayList(Specification.CREATOR);
        this.terms = in.createTypedArrayList(TermsAndConditions.CREATOR);
    }

    public static final Creator<Raffle> CREATOR = new Creator<Raffle>() {
        @Override
        public Raffle createFromParcel(Parcel source) {
            return new Raffle(source);
        }

        @Override
        public Raffle[] newArray(int size) {
            return new Raffle[size];
        }
    };
}