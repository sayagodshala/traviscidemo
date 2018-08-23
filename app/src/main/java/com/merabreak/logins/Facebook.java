package com.merabreak.logins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sayagodshala on 7/8/2015.
 */
public class Facebook implements FacebookCallback<LoginResult> {

    public interface FacebookListener {
        public void onFacebookSuccess(LoginResult loginResult);

        public void onFacebookCancel();

        public void onFacebookError(FacebookException e);
    }

    private FacebookListener facebookListener;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private Context context;

    public static final List<String> FACEBOOK_PERMISSIONS = new ArrayList<String>() {
        {
            add("email");
            add("public_profile");
            //add("user_about_me");
            add("user_birthday");
            add("user_location");
            add("user_hometown");
        }
    };


    public Facebook(Context context, FacebookListener facebookListener) {
        this.facebookListener = facebookListener;
        this.context = context;
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public void login() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, FACEBOOK_PERMISSIONS);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        facebookListener.onFacebookSuccess(loginResult);
    }

    @Override
    public void onCancel() {
        facebookListener.onFacebookCancel();
    }

    @Override
    public void onError(FacebookException e) {
        facebookListener.onFacebookError(e);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
