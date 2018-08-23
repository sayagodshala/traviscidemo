package com.merabreak.activities;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.MainActivity_;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.logins.Facebook;
import com.merabreak.models.APIResponse;
import com.merabreak.models.User;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


/**
 * Created by Ewebcore on 27-Jan-16.
 */
@EActivity(R.layout.mobilenumber_varification)
public class MobileNumberVarification extends BaseActivity implements Facebook.FacebookListener {

    @ViewById(R.id.forgot_password)
    TextView forgotPassword;

    @ViewById(R.id.mobile)
    MaterialEditText mobile;

    @ViewById(R.id.password_otp)
    MaterialEditText passwordOTP;

    @ViewById(R.id.otp_container)
    RelativeLayout otpContainer;

    @ViewById(R.id.regenerate_otp)
    TextView regenerateOtp;

    @ViewById(R.id.facebook_login)
    Button facebookLogin;

    private Facebook facebook;
    private String whichLogin;

    @ViewById(R.id.button_proceed)
    Button buttonProceed;

    @AfterViews
    void init() {

        facebook = new Facebook(this, this);

        BusProvider.bus().register(this);
        passwordOTP.setVisibility(View.GONE);
        regenerateOtp.setVisibility(View.GONE);
        otpContainer.setVisibility(View.GONE);
        forgotPassword.setVisibility(View.GONE);

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

        passwordOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    proceed();
                    return true;
                }
                return false;
            }
        });

       // Util.changeSystemBarColor(this, R.color.black_fade);
    }

    @Click(R.id.button_proceed)
    void proceed() {
        passwordOTP.setError("");
        mobile.setError("");
        if (mobile.getVisibility() == View.VISIBLE) {
            if (isMobileValid()) {
                verifyMobile();
            }
        } else if (passwordOTP.getVisibility() == View.VISIBLE) {
            if (passwordOTP.getHint().toString().equalsIgnoreCase("otp")) {
                if (isOtpValid()) {
                    confirmOtp();
                }
            } else {
                if (isLoginValid()) {
                    login();
                }
            }
        }
    }

    private boolean isLoginValid() {
        String validationMessage = "";

        if (passwordOTP.getText().length() < 6) {
            validationMessage = "Password must be 6 characters long or above";
        }
        if (validationMessage.length() != 0) {
            passwordOTP.setError(validationMessage);
//            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }

    private boolean isMobileValid() {
        String validationMessage = "";
        if (mobile.getText().length() < 10)
            validationMessage = "Mobile not valid!";

        if (validationMessage.length() != 0) {
            mobile.setError(validationMessage);
        }

        return validationMessage.length() == 0;

    }

    private boolean isOtpValid() {
        String validationMessage = "";
        if (passwordOTP.getText().toString().equalsIgnoreCase(""))
            validationMessage = "Please enter OTP!";

        if (validationMessage.length() != 0) {
            passwordOTP.setError(validationMessage);
        }

        return validationMessage.length() == 0;
    }

    private void confirmOtp() {
        hideLoader();
        showLoader();
        Call<APIResponse<User>> callback = apiService.confirmOtp(Constants.MB_API_KEY, mobile.getText().toString().trim(), passwordOTP.getText().toString());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                      //  Util.setMobileVerified(MobileNumberVarification.this);
                        User tempUser = response.body().getValues();
                        tempUser.setPhone(mobile.getText().toString());
                        Util.setUser(MobileNumberVarification.this, tempUser);
                        RegistrationActivity_.intent(MobileNumberVarification.this).mobile(mobile.getText().toString()).start();
                        finish();
                    } else {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
              //  if(t.getMessage()!=null)
                //Log.d("## OTP varification", t.getMessage());
            }
        });
    }

    private void verifyMobile() {

        hideLoader();
        showLoader();

        Call<APIResponse> callback = apiService.registerMobile(Constants.MB_API_KEY, mobile.getText().toString().trim(), "");
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                //Log.d("RegisterMobile", new Gson().toJson(response.body()));
                if (response.body().getMeta() != null) {

                    if (response.body().getMeta().getStatusCode() == 200) {
                        otpContainer.setVisibility(View.VISIBLE);
                        regenerateOtp.setVisibility(View.VISIBLE);
                        passwordOTP.setVisibility(View.VISIBLE);
                        forgotPassword.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        passwordOTP.setHint("OTP");
                        passwordOTP.setHelperText("OTP");
                        buttonProceed.setText("CONFIRM OTP");
                        //Log.d("OTP Success", response.body().getMeta().getMessage());
                        buttonProceed.setText("CONTINUE");
                    } else if (response.body().getMeta().getStatusCode() == 203) {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                    } else if (response.body().getMeta().getStatusCode() == 409) {
                        otpContainer.setVisibility(View.VISIBLE);
                        forgotPassword.setVisibility(View.VISIBLE);
                        passwordOTP.setVisibility(View.VISIBLE);
                        regenerateOtp.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        passwordOTP.setHint("Password");
                        passwordOTP.setHelperText("Password");
                        buttonProceed.setText("CONTINUE");
                    } else {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                    }

                } else {
                    Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
               // if(t.getMessage()!=null)
                //Log.d("## Mobile varification", t.getMessage());
            }
        });

    }

    private void login() {
        hideLoader();
        showLoader();
        Call<APIResponse<User>> callback = apiService.login(Constants.MB_API_KEY, mobile.getText().toString().trim(), passwordOTP.getText().toString());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();

                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                        User user = response.body().getValues();
                        user.setAccountType("merabreak");
                        Util.setUser(MobileNumberVarification.this, user);
                        Util.setUserLogin(MobileNumberVarification.this);
                        MainActivity_.intent(MobileNumberVarification.this).start();
                        finish();
                    } else {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                if(t.getMessage()!=null)
                Util.makeToast(MobileNumberVarification.this, t.getMessage());
            }
        });
    }


    @Subscribe
    public void otpReceived(OTPRecievedEvent otpRecievedEvent) {
        //Log.d("SmsReceiver", otpRecievedEvent.getOtp());
        passwordOTP.setText(otpRecievedEvent.getOtp());
        proceed();
    }

    @Click(R.id.forgot_password)
    void onForgot() {
        ForgotPassword_.intent(this).start();
    }

    @Click(R.id.regenerate_otp)
    void regenerateOTP() {
        verifyMobile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onFacebookSuccess(LoginResult loginResult) {
        hideLoader();
        showLoader();
        //Log.d("Facebook Token", loginResult.getAccessToken().getToken());
        Util.makeToast(MobileNumberVarification.this, "Facebook Loggedin");
        Call<APIResponse<User>> callback = apiService.facebookRegister(Constants.MB_API_KEY, loginResult.getAccessToken().getToken(), loginResult.getAccessToken().getApplicationId());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();
                //Log.d("Facebook Login Response", new Gson().toJson(response.body()));
                if (response.body().getMeta() != null) {


                    User fbUser = null;

                    if (response.body().getMeta().getStatusCode() == 200) {
                        fbUser = response.body().getValues();
                        fbUser.setAccountType("facebook");
                        Util.setUser(MobileNumberVarification.this, fbUser);
                        Util.setUserLogin(MobileNumberVarification.this);
                        MainActivity_.intent(MobileNumberVarification.this).start();
                        finish();
                    } else if (response.body().getMeta().getStatusCode() == 404) {

                        fbUser = response.body().getValues();

                        RegistrationActivity_.intent(MobileNumberVarification.this).fbUser(fbUser).start();
                        finish();


                    } else {
                        Util.makeToast(MobileNumberVarification.this, response.body().getMeta().getMessage());
                    }

                } else {
                    Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(MobileNumberVarification.this, Constants.SOME_THING_WRONG);
              //  if(t.getMessage()!=null)
                //Log.d("Facebook Login Failure", t.getMessage());
            }
        });
    }

    @Override
    public void onFacebookCancel() {

    }

    @Override
    public void onFacebookError(FacebookException e) {
        Util.makeToast(MobileNumberVarification.this, e.getMessage());
    }

    @Click(R.id.facebook_login)
    void onFacebookLogin() {
        showLoader();
        whichLogin = "facebook";
        facebook.login();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (whichLogin.equalsIgnoreCase("facebook")) {
            facebook.onActivityResult(requestCode, resultCode, data);
        } else {
            whichLogin = "";
//            google.onActivityResult(requestCode, resultCode, data);
        }
    }

}
