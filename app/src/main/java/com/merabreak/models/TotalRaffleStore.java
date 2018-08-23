package com.merabreak.models;

/**
 * Created by ETPL-002 on 08-Jan-18.
 */

public class TotalRaffleStore {

    private String total_raffle;
    private String total_store;
    private int rechargeEnabled;

    public TotalRaffleStore() {
    }

    public String getTotal_raffle() {
        return total_raffle;
    }

    public void setTotal_raffle(String total_raffle) {
        this.total_raffle = total_raffle;
    }

    public String getTotal_store() {
        return total_store;
    }

    public void setTotal_store(String total_store) {
        this.total_store = total_store;
    }

    public int getRechargeEnabled() {
        return rechargeEnabled;
    }

    public void setRechargeEnabled(int rechargeEnabled) {
        this.rechargeEnabled = rechargeEnabled;
    }
}
