package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.controls.ItemStep;
import com.merabreak.controls.ItemStep_;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.StepSaveResponse;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Step;
import com.merabreak.timer.MBCountDownTimer;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.challange)
public class ChallengeActivity extends BaseActivity implements MBCountDownTimer.MBTimerListener {

    Animation outAnimation;

    @Extra
    Challenge challange;

    @Extra
    boolean offline = false;

    @Extra
    String slug = "";

    List<Step> steps;
    Step step;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.countdown_timer)
    TextView countdownTimer;

    @ViewById(R.id.background)
    ImageView background;

    @ViewById(R.id.step_container)
    LinearLayout stepContainer;

    @ViewById(R.id.info_container)
    LinearLayout infoContainer;

    @ViewById(R.id.navigations)
    LinearLayout navigations;

    @ViewById(R.id.steppers)
    LinearLayout steppers;

    @ViewById(R.id.challange_title)
    TextView challangeTitle;

    @ViewById(R.id.challange_description)
    TextView challangeDescription;

    @ViewById(R.id.no_steps)
    TextView noSteps;

    @ViewById(R.id.next)
    Button next;

    @ViewById(R.id.start)
    Button start;

    @ViewById(R.id.previous)
    Button previous;

    private int stepCounter = 0;
    ItemStep itemStep;
    ImageLoader imageLoader;
    private MBCountDownTimer mbCountDownTimer;
    long millisUntilFinished = 0;

    private LayoutInflater layoutInflater;
    private int timeLimit = 0;

    private String coins = null;

    @AfterViews
    void init() {
        infoContainer.setVisibility(View.GONE);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(this);
        navigations.setVisibility(TextView.GONE);
        title.setText("Challanges");
        toolbar.setNavigationIcon(R.drawable.button_back1);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        if (offline) {
            challange = DbHelper.getInstance(this).getChallengeById(slug);
            setChallangeInfo();
        } else {
            if (challange != null && !challange.getSlug().equalsIgnoreCase(""))
                getChallengeDetail();
        }

        setBackgrounImage();
    }

    @Click(R.id.start)
    void start() {
        if (offline) {
            startChallenge();
        } else {
            setChallengeStart();
        }
    }

    @Click(R.id.next)
    void onNext() {
        if (!itemStep.isUserCompleted()) {
            Util.makeToast(this, "Please answer this step");
            return;
        }

        if (!itemStep.isWidgetDefined || offline) {
            gotoNextStep();
        } else {
            saveStep();
        }
    }

    private void getChallengeDetail() {
        hideLoader();
        showLoader();
        Call<APIResponse<Challenge>> callback = apiService.getChallengeDetail(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<Challenge>>() {
            @Override
            public void onResponse(Response<APIResponse<Challenge>> response) {
                hideLoader();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            //Log.d("Challenge Data", new Gson().toJson(response.body().getValues()));
                            if (response.body().getValues() != null) {
                                challange = response.body().getValues();
                                setChallangeInfo();
                            }
                        } else {
                            Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                    }
                }else{
                    Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void setChallangeInfo() {
        infoContainer.setVisibility(View.VISIBLE);
        challangeTitle.setText(challange.getTitle());

        if (challange.getSteps() != null && challange.getSteps().size() > 0) {
            noSteps.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            setTimer();
        } else {
            start.setVisibility(View.GONE);
        }
    }

    private void setTimer() {
        timeLimit = challange.getTimeLimit();
        if (timeLimit > 0) {
            long totalMillis = timeLimit * 1000;
            //Log.d("Total Millis", totalMillis + "");
            mbCountDownTimer = new MBCountDownTimer(totalMillis, 1000, this);
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMillis), TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));
            countdownTimer.setText(hms);
        }
    }

    private void setChallengeStart() {
        hideLoader();
        showLoader();
        Call<APIResponse<StartChallenge>> callback = apiService.setChallengeStart(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug());
        callback.enqueue(new Callback<APIResponse<StartChallenge>>() {
            @Override
            public void onResponse(Response<APIResponse<StartChallenge>> response) {
                hideLoader();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            startChallenge();
                        } else {
                            Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                    }
                }else{
                    Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (infoContainer.getVisibility() == View.GONE) {
            if (!itemStep.onBackPressed()) {
                exitAppAlert();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTick(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        this.millisUntilFinished = millis;
        countdownTimer.setText(hms);
    }

    @Override
    public void onFinish() {
        timeUp();
    }

    private void startChallenge() {
        navigations.setVisibility(View.VISIBLE);
        infoContainer.setVisibility(View.GONE);
        steps = challange.getSteps();
        step = steps.get(stepCounter);

        if (mbCountDownTimer != null)
            mbCountDownTimer.start();

        createSteppers();
        setStepView();
    }

    private void gotoNextStep() {
        if (next.getText().toString().equalsIgnoreCase("next")) {
            next.setEnabled(false);
            if (stepCounter >= steps.size() - 1) {
                return;
            }
            steps.set(stepCounter, itemStep.getStep());
            outAnimation = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
            stepContainer.setAnimation(outAnimation);

            challange.setSteps(steps);
            //Log.d("##chlng data aftr next", new Gson().toJson(challange));
            stepCounter++;
            stepContainer.animate();
            if (stepCounter == steps.size() - 1) {
                next.setText("Finish");
            }
            step = steps.get(stepCounter);
            setStepView();
            //Log.d("Step Counter ++", stepCounter + "");
        } else {
            if (offline) {
                submitOfflineChallenge();
            } else {
                submitChallengeAlert();
            }
        }
    }

    private void submitChallengeAlert() {
        if (stepCounter == steps.size() - 1) {
            if (mbCountDownTimer != null) {
                mbCountDownTimer.cancel();
                mbCountDownTimer = null;
            }
            countdownTimer.setVisibility(View.GONE);

            String coinsCollected = (coins != null) ? "! Coin Collected " + coins : "!";

            String thanks = "Thanks for playing this challenge " + coinsCollected;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(thanks).setPositiveButton("Ok", (dialog, which) -> {
                finish();
            }).show();

        }
    }

    private void timeUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your time's up.").setPositiveButton("Ok", (dialog, which) -> {
            finish();
        }).show();
    }

    private void setStepView() {
        if (stepContainer.getChildCount() > 0)
            stepContainer.removeAllViews();
        itemStep = ItemStep_.build(this);
        itemStep.init(step, this, offline, challange.getSlug() , "");
        stepContainer.addView(itemStep);
        next.setAlpha(1);
        next.setEnabled(true);
        activateStepper();
    }

    private void createSteppers() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        for (int i = 0; i < steps.size(); i++) {
            View view = layoutInflater.inflate(R.layout.item_stepper, null);
            view.setLayoutParams(params);
            steppers.addView(view);
        }
    }

    private void activateStepper() {
        for (int i = 0; i < steppers.getChildCount(); i++) {
            View view = steppers.getChildAt(i);
            RelativeLayout tab = (RelativeLayout) view.findViewById(R.id.tab);
            View on = (View) view.findViewById(R.id.on);
            View off = (View) view.findViewById(R.id.off);
            if (stepCounter == i) {
                on.setVisibility(View.VISIBLE);
                off.setVisibility(View.GONE);
            } else {
                off.setVisibility(View.VISIBLE);
                on.setVisibility(View.GONE);
            }
        }
    }

    private void exitAppAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit challange?").setPositiveButton("Yes", (dialog, which) -> {
            if (mbCountDownTimer != null)
                mbCountDownTimer.cancel();
            finish();
        }).setNegativeButton("No", null) //Do nothing on no
                .show();
    }

    private void submitOfflineChallenge() {

        steps.set(stepCounter, itemStep.getStep());
        challange.setSteps(steps);

        hideLoader();
        showLoader();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < challange.getSteps().size(); i++) {
                Step s = challange.getSteps().get(i);
                JSONObject stepObj = new JSONObject();
                stepObj.put("stepId", s.getId());
                if (s.getType().equalsIgnoreCase("quiz"))
                    stepObj.put("answerId", s.getStepAnswer());
                else if (s.getType().equalsIgnoreCase("polling"))
                    stepObj.put("pollingId", s.getStepAnswer());
                jsonArray.put(stepObj);
            }
            jsonObject.put("steps", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StartChallenge startChallenge = Util.getStartChallengeDetails(this);
        Call<APIResponse<StepSaveResponse>> callback;
        callback = apiService.submitChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), jsonObject.toString(), startChallenge.getPlayedId());
        callback.enqueue(new Callback<APIResponse<StepSaveResponse>>() {
            @Override
            public void onResponse(Response<APIResponse<StepSaveResponse>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null) {
                            coins = response.body().getValues().getCoins();
                        }
                        deleteChallengeDirectory(new File(Constants.FOLDER_PATH(challange.getSlug())));
                        submitChallengeAlert();
                    } else {
                        Util.makeToast(ChallengeActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
            }
        });
    }

    public boolean deleteChallengeDirectory(File dir) {
        if (!dir.exists())
            return false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteChallengeDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void saveStep() {
        hideLoader();
        showLoader();
        StartChallenge startChallenge = Util.getStartChallengeDetails(this);
        Call<APIResponse<StepSaveResponse>> callback;

        if (step.getType().equalsIgnoreCase("quiz")) {
            callback = apiService.saveStepQuiz(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), step.getId(), itemStep.getStep().getStepAnswer(), startChallenge.getPlayedId());
        } else if (step.getType().equalsIgnoreCase("polling")) {
            callback = apiService.saveStepPolling(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), step.getId(), itemStep.getStep().getStepAnswer(), startChallenge.getPlayedId());
        } else {
            callback = apiService.saveStep(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), step.getId(), startChallenge.getPlayedId());
        }
        coins = null;
        callback.enqueue(new Callback<APIResponse<StepSaveResponse>>() {
            @Override
            public void onResponse(Response<APIResponse<StepSaveResponse>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 200) {
                        if (response.body().getValues().getCoins() != null)
                            coins = response.body().getValues().getCoins();
                        gotoNextStep();
                    } else if (response.body().getMeta().getStatusCode() == 204) {
                        Util.makeToast(ChallengeActivity.this, response.body().getMeta().getMessage());
                        finish();
                    } else if (response.body().getMeta().getStatusCode() == 409) {
                        gotoNextStep();
                    } else {
                        Util.makeToast(ChallengeActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(ChallengeActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("JSONError", t.getMessage());
            }
        });
    }

    private void setBackgrounImage() {
        String filePath = challange.getBackgroundImage();
        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
                filePath = Constants.FOLDER_PATH(challange.getSlug(), fileName);
                Uri imageURI = Uri.parse(filePath);
                //Log.d("Downloaded IMGURI", filePath);
                background.setImageURI(imageURI);
            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                imageLoader.DisplayImage(filePath, background);
            }
        }
    }
}
