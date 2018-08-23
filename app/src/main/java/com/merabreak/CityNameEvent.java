package com.merabreak;

/**
 * Created by Vinay on 12/23/2016.
 */
public class CityNameEvent {

    private String cityName;

    public CityNameEvent(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
