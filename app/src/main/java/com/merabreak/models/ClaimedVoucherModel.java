package com.merabreak.models;

/**
 * Created by ewebcoremac1 on 06/07/18.
 */

public class ClaimedVoucherModel {

    private String  voucher_code;
    private int  claim_id;
    private String  claim_title;
    private String  claim_description;
    private String thumb_image;
    private String detail_image;

    public String getVoucher_code() {
        return voucher_code;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }

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
}
