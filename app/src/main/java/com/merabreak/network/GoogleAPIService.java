package com.merabreak.network;


import com.merabreak.models.GooglePrediction;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by sayagodshala on 9/15/2015.
 */

public interface GoogleAPIService {


    //    @GET("https://maps.googleapis.com/maps/api/place/autocomplete/json?sensor=false&language=en&key=AIzaSyAiOCq4qJqcqv9TsauOa8knOtwPXmHZeiI&input=")
    @GET("/maps/api/place/autocomplete/json")
    Call<GoogleAPIResponse<List<GooglePrediction>>> getGooglePlaces(@Query("sensor") String sensor,
                                                                    @Query("language") String language,
                                                                    @Query("key") String key,
                                                                    @Query("input") String input,
                                                                    @Query("components") String components);

    @GET("/maps/api/place/autocomplete/json")
    Call<GoogleAPIResponse<List<GooglePrediction>>> getGooglePlacesByLocation(@Query("sensor") String sensor,
                                                                              @Query("language") String language,
                                                                              @Query("key") String key,
                                                                              @Query("input") String input,
                                                                              @Query("location") String location,
                                                                              @Query("radius") int radius,
                                                                              @Query("components") String components);

}