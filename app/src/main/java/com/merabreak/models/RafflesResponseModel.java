package com.merabreak.models;

public class RafflesResponseModel {

    private String title;
    private String image;
    private int price;
    private String banner_image;
    private String prize_text;
    private String brand_link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getPrize_text() {
        return prize_text;
    }

    public void setPrize_text(String prize_text) {
        this.prize_text = prize_text;
    }
    public String getBrand_link() {
        return brand_link;
    }

    public void setBrand_link(String brand_link) {
        this.brand_link = brand_link;
    }
}
