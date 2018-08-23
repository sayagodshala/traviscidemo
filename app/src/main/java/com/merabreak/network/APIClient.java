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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class APIClient {

    private static APIService apiService;
    private static FacebookAPIService facebookAPIService;

    public static APIService getAPIService() {

        OkHttpClient client = new OkHttpClient();
        LoggingInterceptor logging = new LoggingInterceptor();

        client.interceptors().add(new LoggingInterceptor());

        if (apiService == null) {

            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            apiService = restAdapter.create(APIService.class);
        }
        return apiService;
    }

    public static FacebookAPIService getFacebookAPIService() {

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        if (facebookAPIService == null) {
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(Constants.FACEBOOK_GRAPH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            facebookAPIService = restAdapter.create(FacebookAPIService.class);
        }
        return facebookAPIService;
    }

    static class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();

            //Log.d("Retrofit URL", request.toString());
            //Log.d("Retrofit Headers", new Gson().toJson(request.headers()));
            //Log.d("Retrofit Headers", new Gson().toJson(request.body()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            //Log.d("Retrofit Response Time", String.format("Received response for %s in %.1fms%n%s",
               //     response.request().url(), (t2 - t1) / 1e6d, response.headers()) + " " + response.code());

//            if (response.body() != null && response.body().byteStream() != null) {
//                InputStream responseStream = response.body().byteStream();
//                Log.d("Retrofit Response Body", convertStreamToString(responseStream));
//            }
            return response;

        }
    }

    @NonNull
    private static Gson gson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}