package com.merabreak.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.logins.Facebook;
import com.merabreak.models.APIResponse;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.User;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.merabreak.network.Connectivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 8/25/2016.
 */
@EActivity(R.layout.mobile_verification_new)
public class MobileNumberVarificationNew extends BaseActivity implements Facebook.FacebookListener {

    @ViewById(R.id.simple_text)
    TextView simpleLabel;

    @ViewById(R.id.show_hide_text)
    TextView show_hide_text;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.mobile)
    MaterialEditText mobile;

    @ViewById(R.id.password_container)
    RelativeLayout pwContainer;

    @ViewById(R.id.entered_mob)
    TextView enteredMobile;

    @ViewById(R.id.password)
    MaterialEditText password;

    @ViewById(R.id.show_hide_pw)
    Switch showHidePW;

    @ViewById(R.id.forgot_password)
    TextView forgotPW;

    @ViewById(R.id.facebook_login)
    Button facebookLogin;

    private Facebook facebook;
    private String whichLogin;

    @ViewById(R.id.button_proceed)
    Button buttonProceed;

    private static final int MY_PERMISSION_PHONE = 101;
    TelephonyManager manager;
    String deviceId = "not given phone state permission";
    String carrierName = "not given phone state permission";

    @AfterViews
    void init() {
        facebook = new Facebook(this, this);
        pwContainer.setVisibility(View.GONE);

        mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    proceed();
                    return true;
                }
                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    proceed();
                    return true;
                }
                return false;
            }
        });
    }

    private boolean isMobileValid() {
        String validationMessage = "";
        if (mobile.getText().length() >= 1 && mobile.getText().length() < 10)
            validationMessage = getResources().getString(R.string.mobile_verifi_enter_valid_mobile_num);

        if (mobile.getText().length() < 1)
            validationMessage = getResources().getString(R.string.mobile_verifi_enter_mobile_num);

        if (mobile.getText().toString().equalsIgnoreCase("0000000000"))
            validationMessage = getResources().getString(R.string.mobile_verifi_enter_valid_mobile_num);

        if (validationMessage.length() != 0) {
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    private boolean isLoginValid() {
        String validationMessage = "";

        if (password.getText().length() < 6) {
            validationMessage = getResources().getString(R.string.mobile_verifi_enter_six_char_pw);
        }
        if (validationMessage.length() != 0) {
            // password.setError(validationMessage);
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    @Click(R.id.button_proceed)
    void proceed() {
        if (mobile.getVisibility() == View.VISIBLE) {
            if (isMobileValid()) {
                requestPermissions();
                //  verifyMobile();
            }
        } else if (pwContainer.getVisibility() == View.VISIBLE) {
            if (isLoginValid()) {
                login();
            }
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
        if (!hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_PHONE_STATE)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSION_PHONE);
            }
        } else {
            deviceId = Util.uniqueDeviceID(this);
            manager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null && manager.getNetworkOperatorName() != null) {
                carrierName = manager.getNetworkOperatorName();
            }
            if (!carrierName.equalsIgnoreCase("DEFACE"))
                verifyMobile();
            else
                Util.makeToast(this, getResources().getString(R.string.mobile_verifi_deface_sim));
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.mobile_verifi_perm_app_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MobileNumberVarificationNew.this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSION_PHONE);
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
            case MY_PERMISSION_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deviceId = Util.uniqueDeviceID(this);
                    manager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (manager != null && manager.getNetworkOperatorName() != null) {
                        carrierName = manager.getNetworkOperatorName();
                    }
                    if (!carrierName.equalsIgnoreCase("DEFACE"))
                        verifyMobile();
                    else
                        Util.makeToast(this, getResources().getString(R.string.mobile_verifi_deface_sim));
                } else {
                    Util.makeToast(MobileNumberVarificationNew.this, getResources().getString(R.string.mobile_verifi_perm_toast));
                }
                return;
            }
        }
    }

    private void verifyMobile() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.registerMobile(Constants.MB_API_KEY, mobile.getText().toString().trim(), deviceId);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
               hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 200) {
                            OTPVerification_.intent(MobileNumberVarificationNew.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).mobile(mobile.getText().toString()).start();
                        } else if (response.body().getMeta().getStatusCode() == 203) {
                            Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                        } else if (response.body().getMeta().getStatusCode() == 401) {
                            Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                        } else if (response.body().getMeta().getStatusCode() == 409) {
                            pwContainer.setVisibility(View.VISIBLE);
                            mobile.setVisibility(View.GONE);
                            buttonProceed.setBackgroundDrawable
                                    (getResources().getDrawable(R.drawable.button_login));
                            simpleLabel.setText(R.string.login_acc);
                            enteredMobile.setText(mobile.getText().toString());
                        } else {
                            Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                        }

                    } else {
                        Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                hideLoader(progress);
            }
        });
    }

    private void login() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<User>> callback = apiService.login(Constants.MB_API_KEY, mobile.getText().toString().trim(), password.getText().toString());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
            if(response.code() == 200 && response.body() != null) {
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        GoogleAnalyticEvents.eventLogin(MobileNumberVarificationNew.this, gaTracker, "Login", "User Logged In with MeraBreak", mobile.getText().toString() + ": " + new Date().toString());
                       /* Map<String, Object> eventValue = new HashMap<String, Object>();
                        eventValue.put(AFInAppEventParameterName.SUCCESS, "MeraBreak Login");
                        ((ApplicationSingleton) MobileNumberVarificationNew.this.getApplication()).appsFlyerLib().trackEvent(MobileNumberVarificationNew.this, "Login", eventValue);*/
                        //Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                        User user = response.body().getValues();
                        if (user.getSelectedCity() != null) {
                            Util.saveSelectedCity(MobileNumberVarificationNew.this, user.getSelectedCity());
                        } else {
                            Util.saveSelectedCity(MobileNumberVarificationNew.this, "");
                        }
                        user.setAccountType("merabreak");
                        Util.setUser(MobileNumberVarificationNew.this, user);
                        Util.setUserLogin(MobileNumberVarificationNew.this);
                        if (user.getFirstOpened() == 1)
                            Util.setFirstOpened(MobileNumberVarificationNew.this, "true");
                        getDownloadedChallenges();
                        MainActivity_.intent(MobileNumberVarificationNew.this).start();
                        finish();
                    } else {
                        Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                }
            } else {
                    Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void getDownloadedChallenges() {
        user = Util.getUser(this);
        Call<APIResponse<List<DownloadedChallenges>>> callback = apiService.getDownChallenges(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<DownloadedChallenges>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<DownloadedChallenges>>> response) {
            if(response.code() == 200 && response.body() != null) {
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            Util.saveDownChalSlugs(MobileNumberVarificationNew.this, response.body().getValues());
                            //Log.d("downchal response", new Gson().toJson(response.body().getValues()));
                        }
                    } else {
                        Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                }
            } else {
                Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
            }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @CheckedChange(R.id.show_hide_pw)
    void showHidePW() {
        if (showHidePW.isChecked()) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show_hide_text.setText(R.string.fogot_pw_hide);
        } else {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show_hide_text.setText(R.string.fogot_pw_show);
        }
    }

    @Click(R.id.forgot_password)
    void onForgot() {
        ForgotPasswordNew_.intent(this).mobile(mobile.getText().toString()).start();
    }

    @Override
    public void onFacebookSuccess(LoginResult loginResult) {
        hideLoader(progress);
        showLoader(progress);
        //Log.d("Facebook Token", loginResult.getAccessToken().getToken());
        //Log.d("Facebook ApplicationId", loginResult.getAccessToken().getApplicationId());
        Util.makeToast(MobileNumberVarificationNew.this, "Facebook Login Successful");
        Call<APIResponse<User>> callback = apiService.facebookRegister(Constants.MB_API_KEY, loginResult.getAccessToken().getToken(), loginResult.getAccessToken().getApplicationId());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        User fbUser = null;
                        if (response.body().getMeta().getStatusCode() == 200) {
                            GoogleAnalyticEvents.eventLogin(MobileNumberVarificationNew.this, gaTracker, "FB Login", "User Logged In with MeraBreak through FB", new Date().toString());
                          /*  Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_1, "FaceBook Login");
                            ((ApplicationSingleton) MobileNumberVarificationNew.this.getApplication()).appsFlyerLib().trackEvent(MobileNumberVarificationNew.this, "FB Login", eventValue);*/
                            fbUser = response.body().getValues();
                            fbUser.setAccountType("facebook");
                            if (fbUser.getSelectedCity() != null) {
                                Util.saveSelectedCity(MobileNumberVarificationNew.this, fbUser.getSelectedCity());
                            } else {
                                Util.saveSelectedCity(MobileNumberVarificationNew.this, "");
                            }
                            Util.setUser(MobileNumberVarificationNew.this, fbUser);
                            Util.setUserLogin(MobileNumberVarificationNew.this);
                            if (fbUser.getFirstOpened() == 1)
                                Util.setFirstOpened(MobileNumberVarificationNew.this, "true");
                            if (fbUser.getReferOpened() == 1)
                                Util.setReferOpened(MobileNumberVarificationNew.this, "true");
                            getDownloadedChallenges();
                            MainActivity_.intent(MobileNumberVarificationNew.this).start();
                            finish();
                        } else if (response.body().getMeta().getStatusCode() == 404) {

                            fbUser = response.body().getValues();
                            RegistrationActivityNew_.intent(MobileNumberVarificationNew.this).fbUser(fbUser).start();
                            finish();

                        } else {
                            Util.makeToast(MobileNumberVarificationNew.this, response.body().getMeta().getMessage());
                        }

                    } else {
                        Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(MobileNumberVarificationNew.this, Constants.SOME_THING_WRONG);
                if (t.getMessage() != null)
                    Log.d("Facebook Login Failure", t.getMessage());
            }
        });
    }

    @Override
    public void onFacebookCancel() {

    }

    @Override
    public void onFacebookError(FacebookException e) {
        Util.makeToast(MobileNumberVarificationNew.this, e.getMessage());
    }


    @Click(R.id.facebook_login)
    void onFacebookLogin() {
        showLoader(progress);
        whichLogin = "facebook";
        facebook.login();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (whichLogin.equalsIgnoreCase("facebook")) {
            facebook.onActivityResult(requestCode, resultCode, data);
        } else {
            whichLogin = "";
        }
    }
}
