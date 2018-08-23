package com.merabreak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.User;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.merabreak.network.FacebookAPIService;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.subjects.PublishSubject;

public class BaseActivityForYoutube extends YouTubeBaseActivity {

    public User user;
    public FacebookAPIService facebookAPIService;
    public APIService apiService;
    public ImageLoader imageLoader;
    public Tracker gaTracker;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    PublishSubject<Void> detached = PublishSubject.create();
    private AsyncLoader asyncLoader;
    private boolean isInit = false;
    protected Context context;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new ImageLoader(this);
        asyncLoader = AsyncLoader.dialog(this);
        context = this;
        //user = Util.getUser(this);
        initUser();
        gaTracker = ((ApplicationSingleton) getApplication()).googleAnalyticsTracker();
        apiService = APIClient.getAPIService();
        facebookAPIService = APIClient.getFacebookAPIService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        FontUtils.setDefaultFont(this, findViewById(android.R.id.content));
        super.onPostCreate(savedInstanceState);
    }

    protected <T> Observable<T> bindObservable(Observable<T> in) {
        return AppObservable.bindFragment(this, in).takeUntil(detached);
    }

    public void showLoader(LinearLayout progress) {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(LinearLayout progress) {
        progress.setVisibility(View.GONE);
    }

    public void showLoader() {
        try {
            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initUser() {
        user = Util.getUser(this);
       /* if (user != null) {
            AppsFlyerLib.getInstance().setCustomerUserId(user.getPhone());
            AppsFlyerLib.getInstance().setUserEmails(user.getEmail());
        }*/
    }

    public void error(final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> genericError(throwable));
    }

    private void genericError(final Throwable throwable) {
        String errorMessage =
                isNetworkError(throwable) ? "Network error" : "Some error occurred. Try again.";
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        hideLoader();
    }

    private boolean isNetworkError(final Throwable throwable) {
//        if (throwable instanceof RetrofitError) {
//            return ((RetrofitError) throwable).getKind().equals(RetrofitError.Kind.NETWORK);
//        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInit)
            hideLoader();
        isInit = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoader();
    }

    public void oops(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(context.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
            finish();
        }).show();
    }

    public void noNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(context.getResources().getString(R.string.base_youtube_no_internet)).setPositiveButton(context.getResources().getString(R.string.base_youtube_settings), (dialog, which) -> {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }).show();
    }

    public void oopsLogout(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(context.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
            clearAllUserDataAndFinish();
        }).show();
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(this).clearTable();
        SharedPreferences settings = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
    }
}