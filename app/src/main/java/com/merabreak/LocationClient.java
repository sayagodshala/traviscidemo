package com.merabreak;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by sayagodshala on 5/25/2015.
 */

public class LocationClient implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public boolean connectCalled = false;

    public interface LocationClientListener {

        public void onLocationChanged(Location location);

        public void onConnected(Bundle bundle);

        public void onConnectionSuspended(int i);

        public void onConnectionFailed(ConnectionResult connectionResult);

    }

    private static LocationClient instance = null;

    private LocationClientListener mListener;

    private GoogleApiClient googleApiClient;

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // n seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    public Context context;

    public LocationClient(Context context, LocationClientListener listener) {
        this.context = context;
        mListener = listener;
        googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    public static LocationClient init(Context context, LocationClientListener listener) {
        if (instance == null) {
            instance = new LocationClient(context, listener);
        }
        return instance;
    }

    @Override
    public void onLocationChanged(Location location) {
        mListener.onLocationChanged(location);
        //Log.d("LocationClient", location.getLatitude() + " : " + location.getLongitude());
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, REQUEST, this);

        //Log.d("LocationClient", "Location Connected");
        mListener.onConnected(bundle);

        if (LocationServices.FusedLocationApi.getLastLocation(googleApiClient) != null) {
            //Log.d("LocationClient", "Last Location");
            mListener.onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
        } else {
            Log.d("LocationClient", "Last Location is null");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mListener.onConnectionSuspended(i);
        Log.d("LocationClient", "Location Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mListener.onConnectionFailed(connectionResult);
        Log.d("LocationClient", "Location Failed");
    }

    public void connect() {
        connectCalled = true;
        googleApiClient.connect();
        Log.d("LocationClient", "Location Connect");
    }

    public void disconnect() {
        connectCalled = false;
        googleApiClient.disconnect();
        Log.d("LocationClient", "Location Disconnect");
    }
}