package com.merabreak.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.CityUpdateEvent;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.SearchedLocationItem;
import com.merabreak.SearchedLocationItem_;
import com.merabreak.Util;
import com.merabreak.adapter.CitysRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.Cities;
import com.merabreak.models.GooglePrediction;
import com.merabreak.network.Connectivity;
import com.merabreak.network.GoogleAPIClient;
import com.merabreak.network.GoogleAPIResponse;
import com.merabreak.network.GoogleAPIService;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 14-Jan-16.
 */
@EActivity(R.layout.search_location)
public class SearchLocationActivity extends BaseActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    GoogleAPIService googleAPIService;

    @ViewById(R.id.text_sublocality)
    TextView text_sublocality;

    private List<GooglePrediction> currentLocation;
    private GooglePrediction locationObject;

    @ViewById(R.id.edit_search)
    EditText edit_search;

    @ViewById(R.id.linear_location_check)
    LinearLayout linear_location_check;

    @ViewById(R.id.linear_no_results)
    LinearLayout linear_no_results;

    @ViewById(R.id.list_views)
    ListView list_views;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private CitysRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Cities> items = new ArrayList<Cities>();

    private String currentLatLng;

    InputMethodManager imm;

    private static final int ASYNC_GET_PLACES = 1001;
    private static final int ASYNC_GET_LAT_LNG_FROM_PLACE = 1002;

    private String subLocalityLatLng = "";

    private String temporaryLocation = "", sublocality_latlong;

    @ViewById(R.id.progress_spinner)
    ProgressWheel progress_spinner;
    private Toast toast;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient googleApiClient;

    private CustomListAdapter projectItemsAdapter = null;

    @AfterViews
    void init() {
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        title.setText(R.string.search_location_title);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerListItems.setLayoutManager(manager);
        recyclerListItems.setHasFixedSize(false);

        getCities();

        linear_location_check.setVisibility(View.GONE);
        linear_no_results.setVisibility(View.GONE);
        currentLatLng = Util.getLatLng(this);
        currentLocation = new ArrayList<>();
        googleAPIService = GoogleAPIClient.getAPIService();
        progress_spinner.setVisibility(View.GONE);
        //linear_sublocality.setVisibility(View.GONE);
        // edit_search.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

        if (!currentLatLng.equalsIgnoreCase("")) {
            locationObject = new GooglePrediction();
            try {
                locationObject.setDescription(getResources().getString(R.string.search_location_use_currennt_loc));
                locationObject.setLatlng(currentLatLng);
                currentLocation.add(locationObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // edit_search.setHint("Enter Location");
        drawSavedLocationList();
    }

    private void getCities() {
        hideLoader(progress);
        showLoader(progress);
        recyclerListItems.setVisibility(View.GONE);
        Call<APIResponse<List<Cities>>> callback = apiService.getOperationalCities(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Cities>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Cities>>> response) {
                hideLoader(progress);
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(SearchLocationActivity.this, user);
                                getCities();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    items = response.body().getValues();
                                    //Log.d("citys response", new Gson().toJson(items));
                                    drawCities();
                                } else {
                                    Util.makeToast(SearchLocationActivity.this, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(SearchLocationActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(SearchLocationActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(SearchLocationActivity.this, response.body().getMeta().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (Connectivity.isConnected(SearchLocationActivity.this)) {
                    if (t != null)
                        Util.makeToast(SearchLocationActivity.this, t.getMessage());
                } else {
                    Util.makeToast(SearchLocationActivity.this, getResources().getString(R.string.no_internet));
                }
            }
        });
    }

    private void drawCities() {
        recyclerListItems.setVisibility(View.VISIBLE);
        recyclerListItemsAdapter = new CitysRecyclerViewAdapter(items, context);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        ((CitysRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new CitysRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(Cities position, View v) {
                BusProvider.bus().post(new CityUpdateEvent(position.getCityId(), null, position.getCityName()));
                //Log.d("selected city name,id", position.getCityName() + "," + position.getCityId());
                Util.saveLatLng(SearchLocationActivity.this, "");
                finish();
            }
        });
    }

    @TextChange(R.id.edit_search)
    void onLocationSearch(TextView tv, CharSequence text) {
        if (text.length() > 0) {
            linear_no_results.setVisibility(View.VISIBLE);
            recyclerListItems.setVisibility(View.GONE);
            getGooglePlaces();
        } else {
            progress_spinner.setVisibility(View.GONE);
            linear_no_results.setVisibility(View.INVISIBLE);
            recyclerListItems.setVisibility(View.VISIBLE);
            drawSavedLocationList();
        }
    }

    public void getGooglePlaces() {
        progress_spinner.setVisibility(View.VISIBLE);
        Call<GoogleAPIResponse<List<GooglePrediction>>> callBack = null;
        try {
            callBack = googleAPIService.getGooglePlacesByLocation("false", "en", Constants.GOOGLE_APP_API_BROWSER_KEY, URLEncoder.encode(edit_search.getText().toString(), "utf8"), currentLatLng, 3000, "country:in");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callBack.enqueue(new Callback<GoogleAPIResponse<List<GooglePrediction>>>() {
            @Override
            public void onResponse(Response<GoogleAPIResponse<List<GooglePrediction>>> response) {

                progress_spinner.setVisibility(View.GONE);
                //Log.d("Retrofit Response", new Gson().toJson(response.body()));

                List<GooglePrediction> tempPredictions = new ArrayList<GooglePrediction>();
                List<GooglePrediction> predictions = new ArrayList<GooglePrediction>();

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        predictions = response.body().getPredictions();
                        if (predictions.size() > 0) {
                            if (!currentLatLng.equalsIgnoreCase("")) {
                                tempPredictions = new ArrayList<GooglePrediction>();
                                tempPredictions.add(locationObject);
                                for (int i = 0; i < predictions.size(); i++) {
                                    tempPredictions.add(predictions.get(i));
                                }
                            }
                        }
                    }
                    drawSearchedLocationList(tempPredictions);
                } else {
                    drawSearchedLocationList(predictions);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progress_spinner.setVisibility(View.GONE);
                if (t != null)
                    Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });
    }

    private void drawSearchedLocationList(List<GooglePrediction> values) {
        if (values != null && values.size() == 0)
            linear_no_results.setVisibility(View.VISIBLE);
        else {
            linear_no_results.setVisibility(View.GONE);
        }
        projectItemsAdapter = new CustomListAdapter<>(values, this::searchedLocationItem);
        list_views.setAdapter(projectItemsAdapter);
    }


    private void drawSavedLocationList() {
        projectItemsAdapter = new CustomListAdapter<>(currentLocation, this::searchedLocationItem);
        list_views.setAdapter(projectItemsAdapter);
    }

    private SearchedLocationItem searchedLocationItem() {
        SearchedLocationItem item = SearchedLocationItem_.build(this);
        return item;
    }

    @ItemClick(R.id.list_views)
    void onItemClick(GooglePrediction item) {
        if (item.getDescription().contentEquals(getResources().getString(R.string.no_internet))) {
            //Util.makeToast(this, "use my location" + item.getLatlng());
            BusProvider.bus().post(new CityUpdateEvent(null, item.getLatlng(), null));
        } else {
            getLatlongFromAddress(item.getDescription());
        }
    }

    private void getLatlongFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> address;
        //Log.d("Address", strAddress);
        try {
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            Util.makeToast(this, location.getLatitude() + "," + location.getLongitude() + "");
            BusProvider.bus().post(new CityUpdateEvent(null, location.getLatitude() + "," + location.getLongitude(), null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Click(R.id.linear_location_selector)
    void detectLocation() {
        settingsrequest();
    }

    public void settingsrequest() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Util.saveSelectedCity(SearchLocationActivity.this, "");
                        ((ApplicationSingleton) SearchLocationActivity.this.getApplication()).locationClient();
                        finish();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(SearchLocationActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //startLocationUpdates();
                        Util.saveSelectedCity(SearchLocationActivity.this, "");
                        ((ApplicationSingleton) SearchLocationActivity.this.getApplication()).locationClient();
                        finish();
                        break;
                    case Activity.RESULT_CANCELED:
                        //settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
