package com.merabreak.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sayagodshala on 27/01/16.
 */
public class Store implements Parcelable {

    private String id;
    private String title;
    private String slug;
    private String description;
    private String image;
    private String detail_image;
    private String coins;
    private String itemLink;
    private String endDatetime;
    private String productType;
    private String available;
    private String price;
    private List<Specification> specification;
    private String brandName;
    private List<TermsAndConditions> terms;


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

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public List<Specification> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Specification> specification) {
        this.specification = specification;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public List<TermsAndConditions> getTerms() {
        return terms;
    }

    public void setTerms(List<TermsAndConditions> terms) {
        this.terms = terms;
    }

    public Store() {
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
        dest.writeString(this.coins);
        dest.writeString(this.itemLink);
        dest.writeString(this.endDatetime);
        dest.writeString(this.productType);
        dest.writeString(this.available);
        dest.writeString(this.brandName);
        dest.writeString(this.price);
        dest.writeTypedList(this.terms);
        dest.writeTypedList(this.specification);
    }

    protected Store(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.slug = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.detail_image = in.readString();
        this.coins = in.readString();
        this.itemLink = in.readString();
        this.endDatetime = in.readString();
        this.productType = in.readString();
        this.available = in.readString();
        this.brandName = in.readString();
        this.price = in.readString();
        this.terms = in.createTypedArrayList(TermsAndConditions.CREATOR);
        this.specification = in.createTypedArrayList(Specification.CREATOR);
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
