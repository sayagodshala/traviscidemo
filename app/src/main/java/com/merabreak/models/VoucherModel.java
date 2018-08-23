package com.merabreak.models;

import java.util.ArrayList;

/**
 * Created by ewebcoremac1 on 05/07/18.
 */

public class VoucherModel {

    private int notification_id;
    private String notification_title;
    private String notification_sub_title;
    private String notification_description;
    private String notification_type;
    private String claim_status;

    public String getClaim_status() {
        return claim_status;
    }

    public void setClaim_status(String claim_status) {
        this.claim_status = claim_status;
    }

    public ArrayList<ClaimedVoucherModel> getClaim_voucher() {
        return claim_voucher;
    }

    public void setClaim_voucher(ArrayList<ClaimedVoucherModel> claim_voucher) {
        this.claim_voucher = claim_voucher;
    }

    private ArrayList<ClaimedVoucherModel> claim_voucher;

    private ArrayList<ClaimModel> claim_list;

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_sub_title() {
        return notification_sub_title;
    }

    public void setNotification_sub_title(String notification_sub_title) {
        this.notification_sub_title = notification_sub_title;
    }

    public String getNotification_description() {
        return notification_description;
    }

    public void setNotification_description(String notification_description) {
        this.notification_description = notification_description;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public ArrayList<ClaimModel> getClaim_list() {
        return claim_list;
    }

    public void setClaim_list(ArrayList<ClaimModel> claim_list) {
        this.claim_list = claim_list;
    }
}
