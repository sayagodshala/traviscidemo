package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.ReferralText;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 11/16/2016.
 */
@EActivity(R.layout.refer_and_win)
public class ReferAndWinActivity extends BaseActivity {

    ReferralText referralText;
    String shareUrl, shareText;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.refer_skip)
    TextView refer_skip;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.contacts_button)
    Button contacts_button;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

   /* @ViewById(R.id.merabreak_loader)
    ImageView merabreakLoader;*/

    private static final int MY_PERMISSION_CONTACTS = 101;

    @AfterViews
    void init() {
        title.setText(R.string.refer_win_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        if (Connectivity.isConnected(context))
            getReferralText();
        else
            Util.makeToast(context, getResources().getString(R.string.no_internet));

        if(Util.isReferOpened(this))
            refer_skip.setVisibility(View.VISIBLE);

       // Glide.with(this).load(R.raw.merabreak_loader).into(merabreakLoader);
    }

    private void getReferralText() {
        hideLoader(progress);
        showLoader(progress);
       // merabreakLoader.setVisibility(View.VISIBLE);
        Call<APIResponse<ReferralText>> callback = apiService.referalText(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<ReferralText>>() {
            @Override
            public void onResponse(Response<APIResponse<ReferralText>> response) {
                hideLoader(progress);
                try {
                    if(response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(ReferAndWinActivity.this, user);
                                    getReferralText();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        shareText = response.body().getValues().getMessage();
                                    } else {
                                        Util.makeToast(ReferAndWinActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(ReferAndWinActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(ReferAndWinActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(ReferAndWinActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    @Click(R.id.contacts_button)
    void getContactsList() {
        if (Connectivity.isConnected(context)) {
            GoogleAnalyticEvents.event(this, gaTracker, "Contacts Invite", "User decided to invite His Contacts", user.getFullName() + ": " + user.getPhone() + ": " + new Date().toString());
           /* Map<String, Object> eventValue = new HashMap<String, Object>();
            eventValue.put(AFInAppEventParameterName.PARAM_7, "User decided to invite his contacts");
            ((ApplicationSingleton) ReferAndWinActivity.this.getApplication()).appsFlyerLib().trackEvent(ReferAndWinActivity.this, "Contacts Invite", eventValue);*/
            requestPermissions();
        } else {
            Util.makeToast(context, getResources().getString(R.string.no_internet));
        }
    }

    @Click(R.id.invite_button)
    void inviteFriend() {
        if (Connectivity.isConnected(context)) {
            GoogleAnalyticEvents.event(this, gaTracker, "Social Invite", "User Invited His Friends through Social Media", user.getFullName() + ": " + user.getPhone() + ": " + new Date().toString());
            /*Map<String, Object> eventValue = new HashMap<String, Object>();
            eventValue.put(AFInAppEventParameterName.PARAM_8, "User Invited His Friends through Social Media");
            ((ApplicationSingleton) ReferAndWinActivity.this.getApplication()).appsFlyerLib().trackEvent(ReferAndWinActivity.this, "Social Invite", eventValue);*/
            String uri = shareText;
            Util.inviteFriend(this, "MeraBreak", "MeraBreak - Invite Via:", uri);
        } else {
            Util.makeToast(context, getResources().getString(R.string.no_internet));
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {
        if (!hasPermissions(this, android.Manifest.permission.READ_CONTACTS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_CONTACTS);
            }
        } else {
            ReferalContactsActivity_.intent(ReferAndWinActivity.this).start();
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.recharge_perm_app_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(ReferAndWinActivity.this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_CONTACTS);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ReferalContactsActivity_.intent(ReferAndWinActivity.this).start();
                } else {
                    Util.makeToast(ReferAndWinActivity.this, getResources().getString(R.string.recharge_perm_toast));
                }
                return;
            }
        }
    }

    @Click(R.id.refer_skip)
    void skipRefer(){
        sendStatus();
        finish();
    }

    @Override
    public void onBackPressed() {
        sendStatus();
        finish();
    }

    private void sendStatus() {
        Call<APIResponse> callback = apiService.referOepened(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ReferAndWinActivity.this, user);
                                sendStatus();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {

                            }
                        }
                    } else {
                        Util.makeToast(ReferAndWinActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ReferAndWinActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("JSONError", t.getMessage());
            }
        });
    }
}
