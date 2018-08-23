package com.merabreak.network;

import com.merabreak.models.FacebookProfileResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Saya Godshala on 1/13/2016.
 */
public interface FacebookAPIService {

    @GET("/me")
    Call<FacebookProfileResponse> getFacebookProfile(@Query("fields") String fields, @Query("access_token") String accessToken);

}
