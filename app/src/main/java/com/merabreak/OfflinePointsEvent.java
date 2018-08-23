package com.merabreak;

/**
 * Created by Vinay on 11/8/2016.
 */
public class OfflinePointsEvent {

    private int coins;

    public OfflinePointsEvent(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
