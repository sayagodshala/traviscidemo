package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 23-Jan-16.
 */
@EActivity(R.layout.change_password)
public class ChangePasswordActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.old_password)
    MaterialEditText old_password;

    @ViewById(R.id.new_password)
    MaterialEditText new_password;

    @ViewById(R.id.confirm_password)
    MaterialEditText confirm_password;

    @AfterViews
    void init() {
        title.setText(R.string.change_pw_title);
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    changePassword();
                    return true;
                }
                return false;
            }
        });
    }

    @Click(R.id.submit)
    void changePassword() {

        if (isChangePasswordValid()) {
            hideLoader(progress);
            showLoader(progress);
            Call<APIResponse> callback = apiService.changePassword(Constants.MB_API_KEY, user.getAuthToken(), old_password.getText().toString(), new_password.getText().toString());
            callback.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Response<APIResponse> response) {
                    hideLoader(progress);
                    if(response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            Util.makeToast(ChangePasswordActivity.this, response.body().getMeta().getMessage());
                        } else {
                            Util.makeToast(ChangePasswordActivity.this, Constants.SOME_THING_WRONG);
                        }
                    }else{
                        Util.makeToast(ChangePasswordActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    hideLoader(progress);
                    if (t.getMessage() != null) {
                        Log.d("JSONError", t.getMessage());
                        Util.makeToast(ChangePasswordActivity.this, t.getMessage());
                    } else {
                        Log.d("API Error", Constants.SOME_THING_WRONG);
                    }
                }
            });
        }
    }

    private boolean isChangePasswordValid() {
        String validationMessage = "";
        if (old_password.getText().toString().equalsIgnoreCase(""))
            validationMessage = getResources().getString(R.string.enter_old_password);
        else if (new_password.getText().length() < 8)
            validationMessage = getResources().getString(R.string.enter_eight_char_pw);
        else if (!confirm_password.getText().toString().equalsIgnoreCase(new_password.getText().toString().toLowerCase()))
            validationMessage = getResources().getString(R.string.pw_not_matched);

        if (validationMessage.length() != 0) {
            Util.makeToast(this, validationMessage);
        }
        return validationMessage.length() == 0;
    }
}
