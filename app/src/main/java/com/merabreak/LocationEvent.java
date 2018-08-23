package com.merabreak;

import android.location.Location;

/**
 * Created by sayagodshala on 05/07/16.
 */
public class LocationEvent {

    private Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
