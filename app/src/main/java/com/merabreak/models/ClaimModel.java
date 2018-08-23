package com.merabreak.models;

/**
 * Created by ewebcoremac1 on 05/07/18.
 */

public class ClaimModel {

    private int claim_id;
    private String claim_title;
    private String claim_description;
    private String location;
    private int price;
    private String thumb_image;
    private String detail_image;
    private int voucher_left;

    public int getClaim_id() {
        return claim_id;
    }

    public void setClaim_id(int claim_id) {
        this.claim_id = claim_id;
    }

    public String getClaim_title() {
        return claim_title;
    }

    public void setClaim_title(String claim_title) {
        this.claim_title = claim_title;
    }

    public String getClaim_description() {
        return claim_description;
    }

    public void setClaim_description(String claim_description) {
        this.claim_description = claim_description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getDetail_image() {
        return detail_image;
    }

    public void setDetail_image(String detail_image) {
        this.detail_image = detail_image;
    }

    public int getVoucher_left() {
        return voucher_left;
    }

    public void setVoucher_left(int voucher_left) {
        this.voucher_left = voucher_left;
    }
}
