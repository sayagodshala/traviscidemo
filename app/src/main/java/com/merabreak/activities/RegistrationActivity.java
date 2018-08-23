package com.merabreak.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.ForgotPasswordEvent;
import com.merabreak.MainActivity_;
import com.merabreak.OTPRecievedEvent;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.fragments.DatePickerFragment;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.logins.Facebook;
import com.merabreak.models.APIResponse;
import com.merabreak.models.User;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


@EActivity(R.layout.auth)
public class RegistrationActivity extends BaseActivity implements Facebook.FacebookListener, DatePickerFragment.DatePickerFragmentListener {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.facebook_login)
    LinearLayout facebookLogin;

    @ViewById(R.id.spacer_or)
    LinearLayout spacerOr;

    @ViewById(R.id.signup_label)
    TextView signupLabel;

    @ViewById(R.id.full_name)
    EditText fullName;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.mobile)
    EditText mobileEdit;

    @ViewById(R.id.otp)
    EditText otpEdit;

    @ViewById(R.id.or)
    TextView or;

    @ViewById(R.id.regenerate_otp)
    TextView regenerateOTP;

    @ViewById(R.id.button_proceed)
    Button submit;

    @ViewById(R.id.male)
    Button male;

    @ViewById(R.id.female)
    Button female;

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.new_password)
    EditText newPassword;

    @ViewById(R.id.confirm_password)
    EditText confirmPassword;

//    @ViewById(R.id.profile_image)
//    ImageView profileImage;

    @ViewById(R.id.extra_padding)
    View extraPadding;

    @ViewById(R.id.gender_container)
    LinearLayout genderContainer;

    @ViewById(R.id.dob_container)
    LinearLayout dobContainer;

    private Facebook facebook;

    private String whichLogin = "";

    @Extra
    User fbUser;

    @Extra
    String showView = "";

    @Extra
    String mobile = "";

    private ImageLoader mImageLoader;
    private String gender = "";
    private String imagePath = "";

    @AfterViews
    void init() {

        mImageLoader = new ImageLoader(this);

        BusProvider.bus().register(this);

        facebook = new Facebook(this, this);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSubmit();
                    return true;
                }
                return false;
            }
        });

        if (showView.equalsIgnoreCase("login")) {
            facebookLogin.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
        }

        title.setText("Sign Up");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> {
            if (fbUser != null || otpEdit.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                MainActivity_.intent(RegistrationActivity.this).start();
                finish();
            }
        });

        if (fbUser != null) {
            email.setEnabled(false);
            email.setText(fbUser.getEmail());
            fullName.setText(fbUser.getFullName());

            if (fbUser.getGender().equalsIgnoreCase("male")) {
                gender = "male";
                female.setSelected(false);
                male.setSelected(true);
            } else if (fbUser.getGender().equalsIgnoreCase("female")) {
                gender = "female";
                female.setSelected(true);
                male.setSelected(false);
            } else {
                female.setSelected(false);
                male.setSelected(false);
            }

            mobileEdit.requestFocus();

            if (!fbUser.getDob().equalsIgnoreCase("")) {
                try {
                    String[] dates = fbUser.getDob().split("/");
                    date.setText(dates[1] + "/" + dates[0] + "/" + dates[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            mImageLoader.DisplayImage(fbUser.getImage(), profileImage);
            imagePath = fbUser.getImage();

            spacerOr.setVisibility(View.GONE);
            signupLabel.setVisibility(View.GONE);
            facebookLogin.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
//            profileImage.setVisibility(View.VISIBLE);

        } else {
            mobileEdit.setVisibility(View.GONE);
            otpEdit.setVisibility(View.GONE);
        }
    }


    @Click(R.id.facebook_login)
    void onFacebookLogin() {
        whichLogin = "facebook";
        showLoader();
        facebook.login();
    }


    @Override
    public void onFacebookSuccess(LoginResult loginResult) {
        hideLoader();
        showLoader();
        //Log.d("Facebook Token", loginResult.getAccessToken().getToken());
        Util.makeToast(RegistrationActivity.this, "Facebook Loggedin");
        Call<APIResponse<User>> callback = apiService.facebookLogin(Constants.MB_API_KEY, user.getAuthToken(), loginResult.getAccessToken().getToken(), loginResult.getAccessToken().getApplicationId(), mobile);
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();
                //Log.d("Facebook Login Response", new Gson().toJson(response.body()));
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        User newUser = response.body().getValues();

                        spacerOr.setVisibility(View.GONE);
                        signupLabel.setVisibility(View.GONE);
                        facebookLogin.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);

                        fullName.setText(newUser.getFullName());
                        email.setText(newUser.getEmail());
//                        mImageLoader.DisplayImage(newUser.getImage(), profileImage);

                        imagePath = newUser.getImage();

                        if (!newUser.getDob().equalsIgnoreCase("")) {
                            try {
                                String[] dates = newUser.getDob().split("/");
                                date.setText(dates[1] + "/" + dates[0] + "/" + dates[2]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        submit.setText("SAVE");
//                        profileImage.setVisibility(View.VISIBLE);

                        if (newUser.getGender().equalsIgnoreCase("male")) {
                            gender = "male";
                            female.setSelected(false);
                            male.setSelected(true);
                        } else if (newUser.getGender().equalsIgnoreCase("female")) {
                            gender = "female";
                            female.setSelected(true);
                            male.setSelected(false);
                        } else {
                            female.setSelected(false);
                            male.setSelected(false);
                        }
                    } else {
                        Util.makeToast(RegistrationActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(RegistrationActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                whichLogin = "";
                Log.d("Facebook Login Failure", t.getMessage());
            }
        });

    }

    @Override
    public void onFacebookCancel() {
        whichLogin = "";
        Log.d("facebook login", "Canceled");
    }

    @Override
    public void onFacebookError(FacebookException e) {
        whichLogin = "";
        Log.d("facebook login", e.getMessage());
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


    private boolean isSignUpValid() {
        String validationMessage = "";

        if (fbUser != null) {
            if (Util.textIsEmpty(fullName.getText().toString())) {
                validationMessage = "Please enter first name";
            } else if (!Util.isValidEmail(email.getText().toString())) {
                validationMessage = "Please enter valid email";
                email.setError("Enter valid email");
            } else if (Util.textIsEmpty(mobileEdit.getText().toString()) || mobileEdit.getText().toString().length() < 10) {
                validationMessage = "Please enter valid mobile";
                mobileEdit.setError(validationMessage);
            } else if (!male.isSelected() && !female.isSelected()) {
                validationMessage = "Please select gender";
                Util.makeToast(this, validationMessage);
            } else if (date.toString().equalsIgnoreCase("")) {
                validationMessage = "Please select Birth Date";
                Util.makeToast(this, validationMessage);
            }
        } else if (otpEdit.getVisibility() == View.VISIBLE) {

        }

        if (facebookLogin.getVisibility() == View.GONE) {

            if (Util.textIsEmpty(fullName.getText().toString())) {
                validationMessage = "Please enter first name";
            } else if (!Util.isValidEmail(email.getText().toString())) {
                validationMessage = "Please enter valid email";
                email.setError("Enter valid email");
            } else if (!male.isSelected() && !female.isSelected()) {
                validationMessage = "Please select gender";
                Util.makeToast(this, validationMessage);
            } else if (date.toString().equalsIgnoreCase("")) {
                validationMessage = "Please select Birth Date";
                Util.makeToast(this, validationMessage);
            }

        } else {
            if (Util.textIsEmpty(fullName.getText().toString())) {
                validationMessage = "Please enter first name";
            } else if (!Util.isValidEmail(email.getText().toString())) {
                validationMessage = "Please enter valid email";
                email.setError("Enter valid email");
            } else if (password.getText().length() < 8) {
                validationMessage = "Password must be 8 characters long or above";
                password.setError("Password minimum 8 characters");
            } else if (!male.isSelected() && !female.isSelected()) {
                validationMessage = "Please select gender";
                Util.makeToast(this, validationMessage);
            } else if (date.toString().equalsIgnoreCase("")) {
                validationMessage = "Please select Birth Date";
                Util.makeToast(this, validationMessage);
            }
        }


//        if (validationMessage.length() != 0) {
//            Util.makeToast(this, validationMessage);
//        }


        return validationMessage.length() == 0;
    }

    private boolean isFbSignUpValid() {
        String validationMessage = "";

        if (Util.textIsEmpty(fullName.getText().toString())) {
            validationMessage = "Please enter first name";
            fullName.setError(validationMessage);
        } else if (!Util.isValidEmail(email.getText().toString())) {
            validationMessage = "Please enter valid email";
            email.setError("Enter valid email");
        } else if (Util.textIsEmpty(mobileEdit.getText().toString()) || mobileEdit.getText().toString().length() < 10) {
            validationMessage = "Please enter valid mobile";
            mobileEdit.setError(validationMessage);
        } else if (!male.isSelected() && !female.isSelected()) {
            validationMessage = "Please select gender";
            Util.makeToast(this, validationMessage);
        } else if (date.toString().equalsIgnoreCase("")) {
            validationMessage = "Please select Birth Date";
            Util.makeToast(this, validationMessage);
        }

        return validationMessage.length() == 0;
    }

    private boolean isManualSignUpValid() {
        String validationMessage = "";

        if (Util.textIsEmpty(fullName.getText().toString())) {
            validationMessage = "Please enter first name";
            fullName.setError(validationMessage);
        } else if (!Util.isValidEmail(email.getText().toString())) {
            validationMessage = "Please enter valid email";
            email.setError("Enter valid email");
        } else if (password.getText().length() < 8) {
            validationMessage = "Password must be 8 characters long or above";
            password.setError("Password minimum 8 characters");
        } else if (Util.textIsEmpty(mobile)) {
            validationMessage = "Please enter valid mobile";
            Util.makeToast(this, validationMessage);
        } else if (!male.isSelected() && !female.isSelected()) {
            validationMessage = "Please select gender";
            Util.makeToast(this, validationMessage);
        } else if (date.toString().equalsIgnoreCase("")) {
            validationMessage = "Please select Birth Date";
            Util.makeToast(this, validationMessage);
        }

        return validationMessage.length() == 0;
    }

    private boolean isOtpValid() {
        String validationMessage = "";
        if (otpEdit.getText().toString().equalsIgnoreCase(""))
            validationMessage = "Please enter OTP!";

        if (validationMessage.length() != 0) {
            otpEdit.setError(validationMessage);
        }

        return validationMessage.length() == 0;
    }

    @Click(R.id.button_proceed)
    void onSubmit() {

        if (fbUser != null) {
            if (isFbSignUpValid()) {
                authSocialSignup();
            }
        } else if (otpEdit.getVisibility() == View.VISIBLE) {
            if (isOtpValid()) {
                confirmOtp();
            }
        } else if (facebookLogin.getVisibility() == View.GONE) {
            if (isManualSignUpValid()) {
                signup();
            }
        }


//        if (isSignUpValid()) {
//            if (facebookLogin.getVisibility() == View.GONE) {
//                facebookSignUp();
//            } else {
//                signup();
//            }
//        }


    }

    @Click(R.id.male)
    void onMaleClick() {
        male.setSelected(true);
        female.setSelected(false);
        gender = "male";
    }

    @Click(R.id.female)
    void onFeMaleClick() {
        female.setSelected(true);
        male.setSelected(false);
        gender = "female";
    }

    private void signup() {

        hideLoader();
        showLoader();
        Call<APIResponse<User>> callback = apiService.formSignup(Constants.MB_API_KEY,
                user.getAuthToken(),
                fullName.getText().toString().trim(),
                email.getText().toString().trim(),
                mobile,
                password.getText().toString(),
                gender,
                date.getText().toString());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        User user = response.body().getValues();
                        user.setAccountType("merabreak");
                        Util.setUser(RegistrationActivity.this, user);
                        Util.setUserLogin(RegistrationActivity.this);
                        MainActivity_.intent(RegistrationActivity.this).start();
                        finish();
                    } else {
                        Util.makeToast(RegistrationActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(RegistrationActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("onFailure", t.getMessage());
                Util.makeToast(RegistrationActivity.this, t.getMessage());
            }
        });
    }

    private void facebookSignUp() {
        showLoader();
        Call<APIResponse<User>> callBack = apiService.socialSignup(Constants.MB_API_KEY,
                user.getAuthToken(),
                fullName.getText().toString(),
                email.getText().toString(),
                mobile,
                gender,
                date.getText().toString(),
                imagePath);
        callBack.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                //Log.d("facebookSignup", new Gson().toJson(response.body()));
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {

                        User newUser = response.body().getValues();
                        newUser.setPhone(mobile);

                        Util.setUserLogin(RegistrationActivity.this);
                        Util.setUser(RegistrationActivity.this, newUser);
                        MainActivity_.intent(RegistrationActivity.this).start();
                        finish();
                    }
                    Util.makeToast(RegistrationActivity.this, response.body().getMeta().getMessage());
                } else {
                    Util.makeToast(RegistrationActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("socialSignup", t.getMessage());
                Util.makeToast(RegistrationActivity.this, t.getMessage());
            }
        });
    }

    private void authSocialSignup() {
        showLoader();
        Call<APIResponse> callBack = apiService.authSocialSignup(Constants.MB_API_KEY,
                fullName.getText().toString().trim(),
                email.getText().toString().trim(),
                mobileEdit.getText().toString().trim(),
                gender,
                date.getText().toString(),
                imagePath);
        callBack.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                //Log.d("facebookSignup", new Gson().toJson(response.body()));
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 200) {

                        fbUser = null;

                        otpEdit.setVisibility(View.VISIBLE);
                        fullName.setVisibility(View.GONE);
                        email.setVisibility(View.GONE);
                        date.setVisibility(View.GONE);
//                        mobileEdit.setEnabled(false);
                        regenerateOTP.setVisibility(View.VISIBLE);

                        genderContainer.setVisibility(View.GONE);
                        dobContainer.setVisibility(View.GONE);
                    } else if (response.body().getMeta().getStatusCode() == 409) {
                        mobileEdit.requestFocus();
                        Util.makeToast(RegistrationActivity.this, response.body().getMeta().getMessage());
                    }

                } else {
                    Util.makeToast(RegistrationActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                try {
                    Log.d("socialSignup", t.getMessage());
                    Util.makeToast(RegistrationActivity.this, t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Click(R.id.regenerate_otp)
    void onRegenerateOTPClick() {
        if (Util.textIsEmpty(mobileEdit.getText().toString()) || mobileEdit.getText().toString().length() < 10) {
            authSocialSignup();
        }
    }

    private void confirmOtp() {
        hideLoader();
        showLoader();
        Call<APIResponse<User>> callback = apiService.socialConfirmOtp(Constants.MB_API_KEY, mobileEdit.getText().toString().trim(), otpEdit.getText().toString().trim());
        callback.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Response<APIResponse<User>> response) {
                hideLoader();
                //Log.d("ConfirmOTP", new Gson().toJson(response.body()));
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {

                        User newUser = response.body().getValues();
                        newUser.setPhone(mobileEdit.getText().toString());
                        Util.setMobileVerified(RegistrationActivity.this);
                        Util.setUser(RegistrationActivity.this, response.body().getValues());
                        Util.setUserLogin(RegistrationActivity.this);
                        MainActivity_.intent(RegistrationActivity.this).start();
                        finish();

                    } else {
                        Util.makeToast(RegistrationActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(RegistrationActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("## OTP varification", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(this);
    }

    @Subscribe
    public void onForgotPasswordEvent(ForgotPasswordEvent event) {
        if (event.getParam() != null) {
            email.setText(event.getParam());
        }
    }

    @Click(R.id.date)
    void onDate() {
        showDatePickerDialog();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(String d) {
        date.setText(d);
    }

    @Subscribe
    public void otpReceived(OTPRecievedEvent otpRecievedEvent) {
        //Log.d("SmsReceiver", otpRecievedEvent.getOtp());
        if (!otpRecievedEvent.getOtp().equalsIgnoreCase("")) {
            otpEdit.setText(otpRecievedEvent.getOtp());
            confirmOtp();
        }
    }

}