package com.merabreak.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.merabreak.Constants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class GoogleAPIClient {
    private static GoogleAPIService apiService;

    public static GoogleAPIService getAPIService() {

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        if (apiService == null) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_GOOGLE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            apiService = restAdapter.create(GoogleAPIService.class);
        }
        return apiService;
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
//    logger.info(String.format("Sending request %s on %s%n%s",
//        request.url(), chain.connection(), request.headers()));

            //Log.d("Retrofit Request", String.format("Sending request %s on %s%n%s",
             //       request.url(), new Gson().toJson(request.body()), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            //Log.d("Retrofit Response", String.format("Received response for %s in %.1fms%n%s",
            //        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    @NonNull
    private static Gson gson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }


}