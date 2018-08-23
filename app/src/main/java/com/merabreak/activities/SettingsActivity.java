package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.R;
import com.merabreak.models.liveData.LiveScore;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.settings)
public class SettingsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    /*@ViewById(R.id.mobile)
    TextView mobile;

    @ViewById(R.id.email)
    TextView email;

    @ViewById(R.id.score)
    TextView score;

    @ViewById(R.id.score1)
    TextView score1;

    @ViewById(R.id.email_layout)
    LinearLayout email_layout;

    @ViewById(R.id.offline)
    Switch offline;*/

    LiveScore liveScore;

    @ViewById(R.id.team_one_score)
    TextView team_one_score;

    @ViewById(R.id.team_two_score)
    TextView team_two_score;

    String score1, score2;

    @AfterViews
    void init() {
        title.setText(R.string.settings_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        ApplicationSingleton applicationSingleton = (ApplicationSingleton) SettingsActivity.this.getApplication();
//        socket.on("l.ncaa.org.mbasket", onNewData);
//        socket.connect();
       // setData();
    }

//    private Emitter.Listener onNewData = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String data = (String) args[0];
//                    Log.d("Live Score data", new Gson().toJson(args[0]));
//                    Log.d("Live Score data string", data);
//                    JsonParser parser = new JsonParser();
//                    JsonElement mJson =  parser.parse(data);
//                    liveScore = new Gson().fromJson(mJson, LiveScore.class);
//                    Log.d("Live Score datanewvalue", new Gson().toJson(liveScore));
//                    score1 = liveScore.getNew_val().getEvent_score().getTeam1().getTotal_score();
//                    score2 = liveScore.getNew_val().getEvent_score().getTeam2().getTotal_score();
//                    setData();
//                }
//            });
//        }
//    };

//    private void setData(){
//        team_one_score.setText(score1);
//        team_two_score.setText(score2);
//    }

    /*private void setData() {
        mobile.setText(user.getPhone());
        if (user.getEmail() != null) {
            email.setText(user.getEmail());
        } else {
            email_layout.setVisibility(View.GONE);
        }

        if (Util.isMobileDataForOfflineDownload(this))
            offline.setChecked(true);

        offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Util.setOffline(SettingsActivity.this, "true");
                    sendDataStatus(1);
                } else {
                    Util.setOffline(SettingsActivity.this, "");
                    sendDataStatus(0);
                }
            }
        });
    }

    private void sendDataStatus(int data) {
        Call<APIResponse> callback = apiService.dataEnableDesable(Constants.MB_API_KEY, user.getAuthToken(), data);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(SettingsActivity.this, user);
                            sendDataStatus(data);
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {

                        }
                    }
                } else {
                    Util.makeToast(SettingsActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("JSONError", t.getMessage());
            }
        });
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
