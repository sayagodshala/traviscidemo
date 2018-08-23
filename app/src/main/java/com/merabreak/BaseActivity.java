package com.merabreak;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.User;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.merabreak.network.ConnectivityReceiver;
import com.merabreak.network.FacebookAPIService;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.subjects.PublishSubject;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader(this);
        asyncLoader = AsyncLoader.dialog(this);
        // user = Util.getUser(this);
        initUser();
        gaTracker = ((ApplicationSingleton) getApp()).googleAnalyticsTracker();
        apiService = APIClient.getAPIService();
        facebookAPIService = APIClient.getFacebookAPIService();

        context = this;
        getApp().setConnectivityListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        FontUtils.setDefaultFont(this, findViewById(android.R.id.content));
        super.onPostCreate(savedInstanceState);
    }

    protected <T> Observable<T> bindObservable(Observable<T> in) {
        return AppObservable.bindFragment(this, in).takeUntil(detached);
    }

    public void showLoader() {
        try {
            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showLoader(LinearLayout progress) {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(LinearLayout progress) {
        progress.setVisibility(View.GONE);
    }

    public void hideLoader() {
        try {
            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
            asyncLoader.dismiss();
        }
    }

    public void isLoaderHidden() {
        try {
            if(asyncLoader.isShowing()) {
                asyncLoader.hide();
            }
            //Log.d("LoaderHide", String.valueOf(asyncLoader.isShowing()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        initUser();
        if (isInit)
            hideLoader();
        isInit = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoader();
    }

    public void oops(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
            finish();
        }).show();
    }

    public void justAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {

        }).show();
    }

    public void forceUpdate(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(context.getResources().getString(R.string.label_hurray));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
            Util.intentRateUs((Activity) context);
            finish();
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

    private void initUser() {
        user = Util.getUser(this);
       /* if (user != null) {
            AppsFlyerLib.getInstance().setCustomerUserId(user.getPhone());
            AppsFlyerLib.getInstance().setUserEmails(user.getEmail());
        }*/
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(this).clearTable();
        SharedPreferences settings = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public ApplicationSingleton getApp() {
        return ((ApplicationSingleton) getApplication());
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {

        }
    }
}