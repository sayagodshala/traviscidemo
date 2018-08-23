package com.merabreak.service.challenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.merabreak.AppSettings;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.OfflinePointsEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.StepSaveResponse;
import com.merabreak.models.User;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 9/14/2015.
 */
public class ChallengeService extends Service {

    private static final String TAG = "LocationService";
    private Context mContext;
    Activity mActivity;

    APIService apiService;
    private User user = null;
    StartChallenge startChallenge;

    private List<Challenge> challenges;

    private int counter = 0;
    private String coins = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("ChallengeService", "fired");
        //Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        //Log.e(TAG, "onCreate");
        mContext = this;
        mActivity = new Activity();
        try {
            user = Util.getUser(mContext);
            startChallenge = Util.getStartChallengeDetails(mContext);
            if (user != null) {
                challenges = DbHelper.getInstance(mContext).getAllChallengesThatNeedsToBeSubmitted();
               // Log.d("Offline Challenges S", challenges.size() + "");
                if (challenges.size() > 0) {
                    Constants.USER_AUTH_TOKEN = user.getAuthToken();
                    apiService = APIClient.getAPIService();
                    syncOfflineChallenge();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        //Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    private void syncOfflineChallenge() {

        if (challenges.size() == 0)
            return;

        //Log.d("Offline C Completed", new Gson().toJson(challenges.get(0)));

        Call<APIResponse<StepSaveResponse>> callback = apiService.submitChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), challenges.get(counter).getSlug(), challenges.get(counter).getOfflineSavedAnwers(), challenges.get(counter).getPlayedId());
        callback.enqueue(new Callback<APIResponse<StepSaveResponse>>() {
            @Override
            public void onResponse(Response<APIResponse<StepSaveResponse>> response) {
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(mContext, user);
                                syncOfflineChallenge();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                DbHelper.getInstance(mContext).deleteChallengeById(challenges.get(counter).getSlug());
                                challenges.remove(counter);
                                /*coins = response.body().getValues().getCoins();
                                if (coins != null && !coins.equalsIgnoreCase("")) {
                                    AccountDetails accountDetails = Util.getUserAccountDetails(mContext);
                                    if (accountDetails != null) {
                                        int existingCoins = accountDetails.getCoinBalance();
                                        existingCoins += Integer.parseInt(coins);
                                        accountDetails.setCoinBalance(existingCoins);
                                        Util.saveUserAccountDetails(mContext, accountDetails);
                                        BusProvider.bus().post(new OfflinePointsEvent(existingCoins));
                                    } else {
                                        accountDetails = new AccountDetails();
                                        accountDetails.setCoinBalance(Integer.parseInt(coins));
                                        Util.saveUserAccountDetails(mContext, accountDetails);
                                    }
                                }*/
                                syncOfflineChallenge();
                            } else {
                                Util.makeToast(mContext, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(mContext, Constants.SOME_THING_WRONG);
            }
        });
    }

    public void oopsLogout(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(mContext.getResources().getString(R.string.label_ok), (dialog, which) -> {
            clearAllUserDataAndFinish();
        }).show();
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(mContext).clearTable();
        SharedPreferences settings = mContext.getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        mActivity.finish();
    }
}
