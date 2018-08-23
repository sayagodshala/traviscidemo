package com.merabreak;

/**
 * Created by Vinay on 8/11/2016.
 */
public class AmountCoinsEvent {

    private String amount;
    private String coins;
    private String tabName;

    public AmountCoinsEvent(String amount, String coins) {
        this.amount = amount;
        this.coins = coins;
    }

    public AmountCoinsEvent(String amount, String coins, String tabName) {
        this.amount = amount;
        this.coins = coins;
        this.tabName = tabName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
