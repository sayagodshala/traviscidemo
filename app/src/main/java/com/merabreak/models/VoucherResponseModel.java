package com.merabreak.models;

/**
 * Created by ewebcoremac1 on 06/07/18.
 */

public class VoucherResponseModel
{
    private int id;
    private String voucher_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoucher_code() {
        return voucher_code;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }
}
