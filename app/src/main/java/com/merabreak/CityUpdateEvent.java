package com.merabreak;

/**
 * Created by Vinay on 12/13/2016.
 */
public class CityUpdateEvent {

    private String cityId;
    private String cityLatLng;
    private String cityName;

   /* public CityUpdateEvent(String cityId, String cityLatLng) {
        this.cityId = cityId;
        this.cityLatLng = cityLatLng;
    }*/

    public CityUpdateEvent(String cityId, String cityLatLng, String cityName) {
        this.cityId = cityId;
        this.cityLatLng = cityLatLng;
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityLatLng() {
        return cityLatLng;
    }

    public void setCityLatLng(String cityLatLng) {
        this.cityLatLng = cityLatLng;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
