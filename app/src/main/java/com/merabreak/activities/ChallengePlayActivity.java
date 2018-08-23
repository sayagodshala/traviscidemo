package com.merabreak.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivityForYoutube;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.ScreenShott;
import com.merabreak.StepType;
import com.merabreak.Util;
import com.merabreak.Utility;
import com.merabreak.controls.ItemStep;
import com.merabreak.controls.ItemStep_;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.RafflesResponseModel;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.Connectivity;
import com.merabreak.timer.MBCountDownTimer;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * M
 * Created by Ewebcore on 05-Feb-16.
 */

@EActivity(R.layout.challenge_play_new)
public class ChallengePlayActivity extends BaseActivityForYoutube implements MBCountDownTimer.MBTimerListener {

    public static final int RESULT_ADVERTISE = 200;

    Animation outAnimation;

    @Extra
    Challenge challange;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.win_points_hldr)
    RelativeLayout winResultHldr;

    @ViewById(R.id.raffle_coupon_hldr)
    LinearLayout raffleCouponHldr;

    @Extra
    boolean offline = false;

    @Extra
    String slug = "";

    List<Step> steps;
    Step step;

    @ViewById(R.id.countdown_timer_background)
    TextView countdown_timer_background;

    AnimationDrawable timerAnimation;

    @ViewById(R.id.countdown_timer)
    TextView countdownTimer;

    @ViewById(R.id.background)
    ImageView background;

    @ViewById(R.id.feedback_background)
    ImageView feedback_background;

    @ViewById(R.id.step_container)
    LinearLayout stepContainer;

    @ViewById(R.id.relative_main)
    RelativeLayout relative_main;

    @ViewById(R.id.main_play)
    RelativeLayout mainPlayRL;

    @ViewById(R.id.congress_ribbon_hldr)
    RelativeLayout congressRibbonHolder;

    @ViewById(R.id.congress_banner_img)
    ImageView congressBannerImg;

    @ViewById(R.id.steppers)
    LinearLayout steppers;

    @ViewById(R.id.no_steps)
    TextView noSteps;

    @ViewById(R.id.type)
    TextView type;

    @ViewById(R.id.next)
    Button next;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    private int stepCounter = 0;
    ItemStep itemStep;
    ImageLoader imageLoader;
    private MBCountDownTimer mbCountDownTimer;
    long millisUntilFinished = 0;

    private LayoutInflater layoutInflater;
    private int timeLimit = 0;
    private long timeToBlink = 60 * 1000;

    private String coins = null;
    private int msgFlag;

    @ViewById(R.id.play_area)
    LinearLayout playArea;

    @ViewById(R.id.keyboard)
    LinearLayout keyboard;

    @ViewById(R.id.thank_you_page_hldr)
    RelativeLayout feedback1;

    @ViewById(R.id.chall_like)
    Button challLike;

    @ViewById(R.id.capture_screen)
    Button screenCapture;

    @ViewById(R.id.chall_share)
    Button challShare;

    @ViewById(R.id.view_back)
    View view_back;

    @ViewById(R.id.ribbon_congrats)
    ImageView ribbon_congrats;

    @ViewById(R.id.win_title)
    TextView winTitle;

    @ViewById(R.id.win_about)
    TextView winAbout;

    @ViewById(R.id.win_name)
    TextView winName;

    @ViewById(R.id.win_msg)
    TextView winMsg;

    @ViewById(R.id.challenge_right_num)
    TextView challengeRightNum;

    @ViewById(R.id.won_points_num)
    TextView wonPointsNum;

    @ViewById(R.id.won_per_num)
    TextView wonPerNum;

    @ViewById(R.id.challenge_deal)
    TextView challengeDeal;

    @ViewById(R.id.congrats_points)
    TextView congrats_points;

    @ViewById(R.id.congrats_answers)
    TextView congrats_answers;

    @ViewById(R.id.congrats_per)
    TextView congrats_per;

    @ViewById(R.id.empty_view1)
    View empty_view1;

    @ViewById(R.id.empty_view2)
    View empty_view2;

    @ViewById(R.id.chall_done)
    Button challDone;

    @ViewById(R.id.ribbon_heading_txt)
    TextView ribbonHeadingTxt;

    @ViewById(R.id.raffle_voucher_img)
    ImageView voucherImg;

    @ViewById(R.id.raffle_msg)
    TextView raffleMsg;

    @ViewById(R.id.more_challenges_btn)
    Button moreChallengesBtn;

    private static String TAG_MORE_CHALLENGE = "MORE_CHALLENGE";
    private static String TAG_REDEEM_NOW = "REDEEM_NOW";
    private String redeemLink;

    @ViewById(R.id.view_back)
    View viewOverBack;

    private RecyclerView.Adapter recyclerListItemsAdapter;

    public InputStream puzzleBitmapInputStream;

    MediaPlayer mp;

    private Bitmap bitmap;

    private static final int MY_PERMISSION_EXTERNAL_STORAGE = 101;
    int totalPoints;
    int rightAnswerCount;
    int contestRightAnswerCount = 0;
    boolean isAdvertiseFinished = false;

    @AfterViews
    void init() {

        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        toolbar.setVisibility(View.GONE);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*if (android.os.Build.VERSION.SDK_INT >= 21)
            Util.changeSystemBarColor(this, R.color.transparent);*/

        Typeface headingTypeface = Typeface.createFromAsset(ChallengePlayActivity.this.getAssets(), "fonts/tcb.ttf");
        ribbonHeadingTxt.setTypeface(headingTypeface);

        if (challange != null) {
            setBackgrounImage("");
            setChallangeInfo();
            startChallenge();
        } else {
            getLiveChallengeDetail();
        }

        ((ApplicationSingleton) getApplication()).isChallengePlaying = true;

        if (((ApplicationSingleton) getApplication()).isChallengePlaying) {
            Utility.cancelNotification(ChallengePlayActivity.this, 1001);
        }

        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!offline) {
                    ratingBar.setEnabled(false);
                    challengeRating();
                } else {
                    int rate = Math.round(ratingBar.getRating());
                    if (rate > 5)
                        rate = 5;
                    challange.setOfflineRating(rate);
                }
            }
        });*/

    }

    @Click(R.id.view_back)
    void viewOverBackClick()
    {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getLiveChallengeDetail() {
        hideLoader(progress);
        showLoader(progress);

        Call<APIResponse<Challenge>> callback = apiService.getLiveChallengeDetail(Constants.MB_API_KEY, user.getAuthToken(), slug, Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<Challenge>>() {
            @Override
            public void onResponse(Response<APIResponse<Challenge>> response) {
                hideLoader(progress);
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(ChallengePlayActivity.this, user);
                            getLiveChallengeDetail();
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            //Log.d("Live Challenge Data", new Gson().toJson(response.body().getValues()));
                            if (response.body().getValues() != null) {
                                challange = response.body().getValues();
                                setData();
                            } else {
                                //setProblem(response.body().getMeta().getMessage(), "3");
                                Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                            }
                        } else {
                            //oops(response.body().getMeta().getMessage());
                            Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                        }
                    }
                } else {
                    //setProblem(Constants.SOME_THING_WRONG, "2");
                    Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                //setProblem(Constants.SOME_THING_WRONG, "2");
                Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void setData() {
        setBackgrounImage("");
        setChallangeInfo();
        startChallenge();
    }

    @Click(R.id.next)
    public void onNext() {
        if (itemStep != null && !itemStep.isUserCompleted()) {
            Util.makeToast(this, getResources().getString(R.string.missed_something));
            return;
        }

        keyboard.setVisibility(View.GONE);

        if (offline) {
            gotoNextStep();
        } else {
            if (Connectivity.isConnected(this)) {
               // if (!step.getSlug().toLowerCase().contains("advertisement")) {
                if(!step.getSlug().toLowerCase().contains(StepType.ADURI) || !step.getSlug().toLowerCase().contains(StepType.INTERSTITIAL_ADS))
                {
                    //saveStep();
                    saveAllSteps();
                    ((ApplicationSingleton) getApplication()).isChallengePlaying = true;
                } else {
                    gotoNextStep();
                }
            } else {
                Util.makeToast(this, getResources().getString(R.string.no_internet));
                next.setVisibility(View.VISIBLE);
            }
        }
    }

    @Click(R.id.capture_screen)
    void captureScreen() {
        bitmap = ScreenShott.getInstance().takeScreenShotOfView(feedback1);
        if (bitmap != null)
            requestPermissions();
    }

    private void saveScreenShot() {
        try {
            File file = ScreenShott.getInstance()
                    .saveScreenshotToPicturesFolder(ChallengePlayActivity.this, bitmap, "mb_congrats_ss");
            Util.makeToast(ChallengePlayActivity.this, "ScreenShot has been saved to gallery..!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.chall_like)
    void likeChallenge() {
        //  Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //  challLike.startAnimation(shake);
        GoogleAnalyticEvents.eventChallengeLike(this, gaTracker, "Challenge Liked", user.getFullName() + ": " + user.getPhone() + ": " + challange.getTitle());
        /*Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_5, challange.getTitle());
        ((ApplicationSingleton) ChallengePlayActivity.this.getApplication()).appsFlyerLib().trackEvent(ChallengePlayActivity.this, "Challenge Liked", eventValue);*/
        //hideLoader();
        // showLoader();
        Call<APIResponse> callback = apiService.likeChallenge(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                //  hideLoader();
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            challLike.clearAnimation();
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengePlayActivity.this, user);
                                likeChallenge();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                //likeChallenge.setText("Liked");
                                Util.makeToast(ChallengePlayActivity.this, getResources().getString(R.string.challenge_liked));
                                //likeChallenge.clearAnimation();
                            } else {
                                Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // hideLoader();
                Log.d("API Error", Constants.SOME_THING_WRONG);

            }
        });
    }

    @Click(R.id.chall_share)
    void shareChallenge() {
        GoogleAnalyticEvents.eventChallengeLike(this, gaTracker, "Challenge Shared", user.getFullName() + ": " + user.getPhone() + ": " + challange.getTitle());
      /*  Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_6, challange.getTitle());
        ((ApplicationSingleton) ChallengePlayActivity.this.getApplication()).appsFlyerLib().trackEvent(ChallengePlayActivity.this, "Challenge Shared", eventValue);*/
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // .setCanonicalIdentifier("item/12345")
                .setTitle("MeraBreak Challenge")
                //  .setContentDescription("My Content Description")
                //  .setContentImageUrl("https://example.com/mycontent-12345.png")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("action", "open_challenge")
                .addContentMetadata("slug", challange.getSlug());

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing");
        // .addControlParameter("$desktop_url", "http://example.com/home")
        // .addControlParameter("$ios_url", "http://example.com/ios");

        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    //Log.i("MyApp", "got my Branch link to share: " + url);
                    Util.challengeShareApp(ChallengePlayActivity.this, "MeraBreak", "MeraBreak Challenge - Share Via:", url);
                }
            }
        });
    }

    private void setChallangeInfo() {
        if (challange.getSteps() != null && challange.getSteps().size() > 0) {
            noSteps.setVisibility(View.GONE);
            setTimer();
        }
        if (challange.getBackgroundColor() != null)
            relative_main.setBackgroundColor(Color.parseColor(challange.getBackgroundColor()));
        //setBackgrounImage();
        setEndBackgroundImage();
    }

    private void setTimer() {
        timeLimit = challange.getTimeLimit();
        if (timeLimit > 0) {
            long totalMillis = timeLimit * 1000;
             millisUntilFinished = totalMillis;
            // Log.d("Total Millis", totalMillis + "");
            mbCountDownTimer = new MBCountDownTimer(totalMillis, 1000, this);
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));
            countdownTimer.setText(hms);
            }
    }

    @Override
    public void onBackPressed() {
        if (feedback1.getVisibility() == View.VISIBLE) {
            if (Connectivity.isConnected(this)) {
                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            } else {
                OfflineChallengesActivity_.intent(this).start();
            }
        }
        quitChallenge();
    }

    @Override
    public void onTick(long millis) {
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        this.millisUntilFinished = millis;
        if (millis < timeToBlink) {
            countdownTimer.setTextColor(getResources().getColor(R.color.app_red));
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            countdownTimer.startAnimation(animation);
            countdown_timer_background.setBackgroundResource(R.drawable.back_animation);
            timerAnimation = (AnimationDrawable) countdown_timer_background.getBackground();
            timerAnimation.start();
        }
        countdownTimer.setText(hms);
    }

    @Override
    public void onFinish() {
        if (!((Activity) context).isFinishing()) {
            long millis = 0000;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            this.millisUntilFinished = millis;
            if (millis < timeToBlink) {
                timerAnimation.stop();
            }
            countdownTimer.setText(hms);
            timeUp();
        }
    }


    private void startChallenge() {
        steps = challange.getSteps();
        if (steps.size() > 0 && stepCounter < steps.size()) {
            step = steps.get(stepCounter);
            if (step.getSlug().contains("advertisement"))
                next.setText(R.string.skip);
            else
                next.setText(R.string.next);

            if (mbCountDownTimer != null) {
                mbCountDownTimer.start();
            }

            if (steps.size() > 1) {
                createSteppers();
            }
            setStepView();
        } else {
            Util.makeToast(context, Constants.SOME_THING_WRONG);
        }
    }


    private void gotoNextStep() {
        if (stepCounter < steps.size() - 1) {
            steps.set(stepCounter, itemStep.getStep());
            outAnimation = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in_new);
            //outAnimation = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_from_middle);
            stepContainer.setAnimation(outAnimation);
            // mp = MediaPlayer.create(this, R.raw.slide_transition);
            //  mp.startBtn();
            challange.setSteps(steps);
            //Log.d("##chlng data aftr next", new Gson().toJson(challange));
            if (step.getPoints() > -1)
                totalPoints += step.getPoints();
            if (step.getPoints() >= 0) {
                rightAnswerCount += 1;
            }
            if(step.getRightAnswer()!=null && !step.getRightAnswer().toString().isEmpty() && step.getRightAnswer().equals("1"))
            {
                ++contestRightAnswerCount;
            }

            stepCounter++;
            stepContainer.animate();
            step = steps.get(stepCounter);
            setStepView();
        } else {
            if (step.getPoints() > -1)
                totalPoints += step.getPoints();
            if (step.getPoints() >= 0) {
                rightAnswerCount += 1;
            }
            if(step.getRightAnswer()!=null && !step.getRightAnswer().toString().isEmpty() && step.getRightAnswer().equals("1"))
            {
                ++contestRightAnswerCount;
            }
            if (offline) {
                relative_main.setBackgroundColor(getResources().getColor(R.color.app_background_new));
                submitOfflineChallenge();
            } else {
                //postPlayStep();
                relative_main.setBackgroundColor(getResources().getColor(R.color.app_background_new));

                if(Integer.valueOf(challange.getContest_challenge()) == 1 )
                {
                    String isRight;
                    //if(rightAnswerCount == steps.size())
                   // if(contestRightAnswerCount == steps.size()) // code need to change June 29, 2018
                    if(contestRightAnswerCount == challange.getCountQuestion())
                    {
                        isRight = "1";
                    }else {
                        isRight = "0";
                    }
                    postChallengeLastResult(isRight);

                    screenAfterCompletingRaffle("",null);
                }
                else
                {
                    submitChallengeAlert();
                }

            }
        }
    }

    private int percentageCalculationForRaffle()
    {
        return (int) Math.round(contestRightAnswerCount * 100 / challange.getCountQuestion());
    }

    private int percentageCalculation() {
        double totalChallengePoints = (double) challange.getCoins();
        // double wonCoins = Double.parseDouble(coins);
        double wonPoints = (double) totalPoints;
        int totPerc = (int) Math.round(wonPoints * 100 / totalChallengePoints);
        //winPerc.setText(getResources().getString(R.string.win_per) +""+ String.valueOf(totPerc));
        return totPerc;
    }

    private void submitChallengeAlert() {
        ((ApplicationSingleton) getApplication()).isChallengePlaying = false;
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        winTitle.setTypeface(font);
        winMsg.setTypeface(font);
        congrats_points.setTypeface(font);
        congrats_answers.setTypeface(font);
        congrats_per.setTypeface(font);
        challengeRightNum.setTypeface(font);
        wonPointsNum.setTypeface(font);
        wonPerNum.setTypeface(font);
        if (challange.getDealFlag() == 1) {
            challengeDeal();
        } else if (challange.getDealFlag() == 2) {
            challengeDeal.setVisibility(View.VISIBLE);
        }

        if (challange.getIsLiveChallenge() == 0) {
            if (stepCounter == steps.size() - 1) {
                if (mbCountDownTimer != null) {
                    mbCountDownTimer.cancel();
                    mbCountDownTimer = null;
                }

                feedback1.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                background.setVisibility(View.GONE);
                countdownTimer.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                keyboard.setVisibility(View.GONE);
                winResultHldr.setVisibility(View.VISIBLE);
                raffleCouponHldr.setVisibility(View.GONE);



                if (user.getFullName() != null) {
                    //  winnerName.setText(user.getFullName());
                    winName.setText(user.getFullName()+", ");
                    winAbout.setText(getResources().getString(R.string.you_won));
                }
                challengeRightNum.setText(String.valueOf(rightAnswerCount));
                if (challange.getCanReplay() != 0) {
                    challDone.setText(R.string.replay);
                }
                if (totalPoints > 0) {
                    AccountDetails accountDetails = Util.getUserAccountDetails(this);
                    if (accountDetails != null) {
                        int existingCoins = accountDetails.getCoinBalance();
                        existingCoins += totalPoints;
                        accountDetails.setCoinBalance(existingCoins);
                        Util.saveUserAccountDetails(this, accountDetails);
                    } else {
                        accountDetails = new AccountDetails();
                        accountDetails.setCoinBalance(totalPoints);
                        Util.saveUserAccountDetails(this, accountDetails);
                    }
                    wonPointsNum.setText(String.valueOf(totalPoints));
                    wonPerNum.setText(String.valueOf(percentageCalculation()) + "" + "%");

                }
                if (totalPoints == 0) {
                    winName.setText(user.getFullName()+", ");
                    winAbout.setText(getResources().getString(R.string.another_chance));
                    winTitle.setText(R.string.better_luck);
                    winTitle.setTextColor(Color.parseColor("#d43e3e"));
                    winMsg.setText(R.string.dont_worry);
                }
            }
        } else {
            if (stepCounter == steps.size() - 1) {
                if (mbCountDownTimer != null) {
                    mbCountDownTimer.cancel();
                    mbCountDownTimer = null;
                }

                feedback1.setVisibility(View.VISIBLE);
                //toolbar.setVisibility(View.VISIBLE);
                background.setVisibility(View.GONE);
                countdownTimer.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                keyboard.setVisibility(View.GONE);

                if(contestRightAnswerCount == 1 )
                {
                    String isRight;
                    //if(rightAnswerCount == steps.size()-1)
                    if(Integer.valueOf(step.getRightAnswer()) == challange.getCountQuestion())
                    {
                        isRight = "1";
                    }else
                        isRight = "0";
                    postChallengeLastResult(isRight);
                }

            }
        }
    }

    private void screenAfterCompletingRaffle(String message, RafflesResponseModel rafflesResponseModel)
    {
        ((ApplicationSingleton) getApplication()).isChallengePlaying = false;
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        winTitle.setTypeface(font);
        winMsg.setTypeface(font);
        congrats_points.setTypeface(font);
        congrats_answers.setTypeface(font);
        congrats_per.setTypeface(font);
        challengeRightNum.setTypeface(font);
        wonPointsNum.setTypeface(font);
        wonPerNum.setTypeface(font);
        if (challange.getDealFlag() == 1) {
            challengeDeal();
        } else if (challange.getDealFlag() == 2) {
            challengeDeal.setVisibility(View.VISIBLE);
        }

        // set Banner image

        if(rafflesResponseModel != null && !Util.textIsEmpty(rafflesResponseModel.getBanner_image()))
        {
            congressRibbonHolder.setVisibility(View.INVISIBLE);
            congressBannerImg.setVisibility(View.VISIBLE);

            Picasso.with(ChallengePlayActivity.this).load(rafflesResponseModel.getBanner_image()).placeholder(R.drawable.dummy_challenge_image).into(congressBannerImg, new com.squareup.picasso.Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }else
        {
            congressRibbonHolder.setVisibility(View.VISIBLE);
            congressBannerImg.setVisibility(View.INVISIBLE);
        }

        if (challange.getIsLiveChallenge() == 0) {
            if (stepCounter == steps.size() - 1) {
                if (mbCountDownTimer != null) {
                    mbCountDownTimer.cancel();
                    mbCountDownTimer = null;
                }

                feedback1.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                background.setVisibility(View.GONE);
                countdownTimer.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                keyboard.setVisibility(View.GONE);
                winResultHldr.setVisibility(View.VISIBLE);
                winAbout.setVisibility(View.VISIBLE);

                challengeRightNum.setText(String.valueOf(rightAnswerCount));
                if (challange.getCanReplay() != 0) {
                    challDone.setText(R.string.replay);
                }

                Typeface headingTypeface = Typeface.createFromAsset(ChallengePlayActivity.this.getAssets(), "fonts/tcb.ttf");
                ribbonHeadingTxt.setTypeface(headingTypeface);

                int raffleResult = percentageCalculationForRaffle();

                if(raffleResult >=0 && raffleResult <25)
                {
                    ribbon_congrats.setImageResource(R.drawable.tough_luck_icon);
                    ribbonHeadingTxt.setText(getResources().getString(R.string.tough_luck));
                }else if(raffleResult >=25 && raffleResult <50)
                {
                    ribbon_congrats.setImageResource(R.drawable.can_do_better_icon);
                    ribbonHeadingTxt.setText(getResources().getString(R.string.can_do_better));
                }
                else if(raffleResult >=50 && raffleResult <75)
                {
                    ribbon_congrats.setImageResource(R.drawable.almost_there_icon);
                    ribbonHeadingTxt.setText(getResources().getString(R.string.almost_there));
                }
                else if(raffleResult >=75 && raffleResult <99)
                {
                    ribbon_congrats.setImageResource(R.drawable.one_step_closer_icon);
                    ribbonHeadingTxt.setText(getResources().getString(R.string.one_step_closer));
                }else if(raffleResult==100)
                {
                    ribbon_congrats.setImageResource(R.drawable.genius_icon);
                    ribbonHeadingTxt.setText(getResources().getString(R.string.genius));
                }


                if(Integer.valueOf(challange.getContest_challenge()) == 1 )
                {
                    winResultHldr.setVisibility(View.GONE);
                    raffleCouponHldr.setVisibility(View.VISIBLE);
                    congrats_points.setVisibility(View.GONE);
                    wonPointsNum.setVisibility(View.GONE);
                    empty_view1.setVisibility(View.GONE);
                    challDone.setVisibility(View.GONE);


                    if(rafflesResponseModel != null ) {
                        moreChallengesBtn.setVisibility(View.VISIBLE);
                        redeemLink = rafflesResponseModel.getBrand_link();
                        if (!Util.textIsEmpty(rafflesResponseModel.getBrand_link())) {
                            moreChallengesBtn.setText(getResources().getString(R.string.redeem_now));
                            moreChallengesBtn.setTag(TAG_REDEEM_NOW);
                        } else {
                            moreChallengesBtn.setText(getResources().getString(R.string.more_challenges));
                            moreChallengesBtn.setTag(TAG_MORE_CHALLENGE);
                        }
                    }
                    else
                        moreChallengesBtn.setVisibility(View.GONE);

                    if(rafflesResponseModel != null && rafflesResponseModel.getImage() != null && !rafflesResponseModel.getImage().isEmpty()) {

                        voucherImg.setVisibility(View.VISIBLE);
                        Picasso.with(ChallengePlayActivity.this).load(rafflesResponseModel.getImage()).placeholder(R.drawable.merabreak_placeholder_img).into(voucherImg, new com.squareup.picasso.Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                            }
                        });
                    }
                    else {
                        voucherImg.setVisibility(View.GONE);
                    }


                    String isRight;
                   // if(rightAnswerCount == steps.size())
                   // if(contestRightAnswerCount == steps.size()) //code need to change June 29, 2018
                    if(contestRightAnswerCount == challange.getCountQuestion())
                    {
                        winTitle.setText(R.string.congrats);

                        if (user.getFullName() != null) {

                            StringBuilder name = new StringBuilder();
                            /*name.append(Html.fromHtml("<b>"+user.getFullName()+", "+"</b>"))
                                .append(getResources().getString(R.string.you_won));*/

                            winName.setText(user.getFullName()+",");
                            winAbout.setText(getResources().getString(R.string.you_won));
                        }


                        if(message!=null && !message.isEmpty())
                            winMsg.setText(message);
                        else
                            winMsg.setText(getResources().getString(R.string.on_raffle_success));


                        if(rafflesResponseModel != null && rafflesResponseModel.getPrize_text() != null && !rafflesResponseModel.getPrize_text().isEmpty())
                        {
                            raffleMsg.setVisibility(View.VISIBLE);

                            /*StringBuilder sb = new StringBuilder();
                            sb.append(getResources().getString(R.string.you_are_eligible))
                                    .append(rafflesResponseModel.getTitle())
                                    .append(" worth ")
                                    .append(Html.fromHtml("Rs.<b>"+rafflesResponseModel.getPrice()+"</b>"))
                                    .append("/-,")
                                    .append(" winners will announce soon");

                            raffleMsg.setText(sb);*/

                            raffleMsg.setText(Util.getHtmlFormatString(rafflesResponseModel.getPrize_text()));
                        }
                        else {
                            raffleMsg.setVisibility(View.GONE);
                        }
                    }else {
                        //winTitle.setText(R.string.better_luck);
                        winTitle.setText(R.string.uh_oh_failure);
                        winTitle.setTextColor(Color.parseColor("#d43e3e"));

                        if (user.getFullName() != null) {

                            winName.setText(user.getFullName()+",");
                            winAbout.setText(getResources().getString(R.string.dont_wry));
                           // winAbout.setText(getResources().getString(R.string.you_can_do_better));
                        }

                        if(message!=null && !message.isEmpty())
                            winMsg.setText(message);
                        else
                            winMsg.setText(getResources().getString(R.string.on_raffle_unsuccess));

                        if(rafflesResponseModel != null && rafflesResponseModel.getPrize_text() != null && !rafflesResponseModel.getPrize_text().isEmpty())
                        {

                            /*raffleMsg.setVisibility(View.VISIBLE);
                            StringBuilder sb = new StringBuilder();
                            sb.append("You are so close to win ")
                                    .append(rafflesResponseModel.getTitle())
                                    .append(" worth ")
                                    .append(Html.fromHtml("Rs.<b>" + rafflesResponseModel.getPrice() + "</b>"))
                                    .append("/-,")
                                    .append(" all the best for next contest");

                            raffleMsg.setText(sb);*/

                            raffleMsg.setVisibility(View.VISIBLE);
                            raffleMsg.setText(Util.getHtmlFormatString(rafflesResponseModel.getPrize_text()));
                        }else {
                            raffleMsg.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } else {
            if (stepCounter == steps.size() - 1) {
                if (mbCountDownTimer != null) {
                    mbCountDownTimer.cancel();
                    mbCountDownTimer = null;
                }
                feedback1.setVisibility(View.VISIBLE);
                //toolbar.setVisibility(View.VISIBLE);
                background.setVisibility(View.GONE);
                countdownTimer.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                keyboard.setVisibility(View.GONE);

                if(Integer.valueOf(challange.getContest_challenge()) == 1 )
                {
                    String isRight;
                 //   if(contestRightAnswerCount == steps.size()) //code need to change June 29, 2018
                    if(contestRightAnswerCount == challange.getCountQuestion())
                    {
                        isRight = "1";
                    }else
                        isRight = "0";
                }

            }
        }
    }

    private void submitChallengeOfflineAlert() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        winTitle.setTypeface(font);
        winMsg.setTypeface(font);
        congrats_answers.setVisibility(View.GONE);
        congrats_per.setVisibility(View.GONE);
        congrats_points.setVisibility(View.GONE);
        challengeRightNum.setVisibility(View.GONE);
        wonPointsNum.setVisibility(View.GONE);
        wonPerNum.setVisibility(View.GONE);
        empty_view1.setVisibility(View.GONE);
        empty_view2.setVisibility(View.GONE);
        if (challange.getDealFlag() == 1) {
            challengeDeal();
        } else if (challange.getDealFlag() == 2) {
            challengeDeal.setVisibility(View.VISIBLE);
        }

        // if (mp != null)
        //     mp.release();
        if (stepCounter == steps.size() - 1) {
            if (mbCountDownTimer != null) {
                mbCountDownTimer.cancel();
                mbCountDownTimer = null;
            }
            countdownTimer.setVisibility(View.GONE);
            feedback1.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            background.setVisibility(View.GONE);
            playArea.setVisibility(View.GONE);
            steppers.setVisibility(View.GONE);
            next.setVisibility(View.GONE);

            winAbout.setText(getResources().getString(R.string.offline_cha_thanks));
            if (user.getFullName() != null) {
                // winnerName.setText(user.getFullName());
                winName.setText(user.getFullName()+", ");
            } else {
                winName.setVisibility(View.GONE);
                // winnerName.setVisibility(View.GONE);
            }
            // wonPoints.setVisibility(View.GONE);
            //  youWon.setVisibility(View.GONE);
            keyboard.setVisibility(View.GONE);
            //   extraText3.setVisibility(View.VISIBLE);
            if (Connectivity.isConnected(context)) {
                // extra_text_3.setText("Will sync Your IPLPointsConfig");
                winMsg.setText(R.string.offline_cha_sync_with_internet);
            } else {
                // extra_text_3.setText("Will sync your won points immediately when internet comes");
                winMsg.setText(R.string.offline_cha_sync_no_internet);
            }
            //   likeChallenge.setVisibility(View.GONE);
            //  shareChallenge.setVisibility(View.GONE);
            challLike.setVisibility(View.GONE);
            challShare.setVisibility(View.GONE);
            screenCapture.setVisibility(View.GONE);
            //see_all_challenges1.setVisibility(View.GONE);
            // winPerc.setVisibility(View.GONE);
            // challScoreContent.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) challDone.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 100);
            challDone.setLayoutParams(layoutParams);
            //challDone.setGravity(Gravity.CENTER);

            if (challange.getCanReplay() == 0)
                challDone.setText(R.string.done);

        }
    }

    @Click(R.id.challenge_deal)
    void dealDetails() {
        challengeDeal();
    }

    private void challengeDeal() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialoglayout = layoutInflater.inflate(R.layout.deal_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView dealTitle = (TextView) dialoglayout.findViewById(R.id.deal_name);
        TextView dealCode = (TextView) dialoglayout.findViewById(R.id.deal_code);
        TextView aboutDeal = (TextView) dialoglayout.findViewById(R.id.about_deal);
        TextView dealTC = (TextView) dialoglayout.findViewById(R.id.deal_tc);
        ImageView dealImage = (ImageView) dialoglayout.findViewById(R.id.deal_image);

        dealTitle.setText(challange.getDealTitle());
        dealCode.setText(challange.getDealCode());
        aboutDeal.setText(challange.getDealAbout());
        dealTC.setText(challange.getDealTC());
        String filePath = challange.getDealImage();

        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = challange.getDealImage().substring(challange.getDealImage().lastIndexOf('/') + 1, challange.getDealImage().length());
                String savedFile = Constants.FOLDER_PATH(challange.getSlug(), fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    dealImage.setImageURI(imageURI);
                } else {
                    // Util.makeToast(context, "Image Not found");
                }
            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(challange.getDealImage()).into(dealImage, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        }

        builder.setView(dialoglayout);
        AlertDialog alertDialog = builder.show();
        dialoglayout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    @Click(R.id.chall_done)
    void seeAllChallenges1() {
        // MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).startBtn();

        if (challange.getCanReplay() == 0) {
            if (Connectivity.isConnected(this)) {
                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            } else {
                OfflineChallengesActivity_.intent(this).start();
            }
        } else {
            if (Connectivity.isConnected(this)) {
                if (challange.getChallengeType().equalsIgnoreCase("article"))
                    ArticleActivity_.intent(this).challenge(challange).start();
                else
                    ChallengeDetailActivity_.intent(this).slug(challange.getSlug()).start();
                finish();
            } else {
                OfflineChallengesActivity_.intent(this).start();
            }
        }
    }

    @Click(R.id.more_challenges_btn)
    void gotMoreChallenges()
    {
        if(moreChallengesBtn.getTag().equals(TAG_MORE_CHALLENGE)) {
            if (Connectivity.isConnected(this)) {
                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            } else {
                OfflineChallengesActivity_.intent(this).start();
            }
        }else if(moreChallengesBtn.getTag().equals(TAG_REDEEM_NOW)) {
            InAppBrowserActivity_.intent(ChallengePlayActivity.this).url(redeemLink).toolbarTitle(getResources().getString(R.string.redeem_now)).start();
            finish();
        }
    }

    private void timeUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.time_up)).setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
            if (Connectivity.isConnected(this)) {
                ((ApplicationSingleton) getApplication()).isChallengePlaying = false;
                //   if (mp != null)
                //      mp.release();
                MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
            } else {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    private void offlineSubmitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Thanks for playing offline challenge").setPositiveButton("Ok", (dialog, which) -> {
            MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        }).show();
    }

    private void setStepView() {
        if (stepContainer.getChildCount() > 0)
            stepContainer.removeAllViews();

        if(step.getSlug().toLowerCase().equals(StepType.ADURI)
                || step.getSlug().toLowerCase().equals(StepType.INTERSTITIAL_ADS))
        {
            /*try {
                mbCountDownTimer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            mainPlayRL.setVisibility(View.GONE);
            next.setVisibility(View.GONE);

            if (mbCountDownTimer != null) {
                mbCountDownTimer.cancel();
                mbCountDownTimer = null;
            }
            AdvertisingActivity_.intent(this).step(step).challengeSlug(challange.getSlug()).offline(offline).contentLanguage(challange.getContentLanguage()).startForResult(RESULT_ADVERTISE);

            //stepCounter++;

        }
        else {
            mainPlayRL.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);

            itemStep = ItemStep_.build(this);
            itemStep.init(step, this, offline, challange.getSlug(), challange.getContentLanguage());
            stepContainer.addView(itemStep);

            next.setVisibility(View.VISIBLE);

            FontUtils.setDefaultFont(this, stepContainer);

            next.setAlpha(1);
            next.setEnabled(true);
            type.setText(step.getType());

            setBackgrounImage(step.getBackgroundImage());


            if (step.getSlug().contains("advertisement"))
                next.setText(R.string.skip);
            else
                next.setText(R.string.next);
            if (step.getType().equalsIgnoreCase(StepType.RIDDLE)) {
                keyboard.setVisibility(View.VISIBLE);
            }
            if (step.getType().equalsIgnoreCase(StepType.JIGSAW)) {
                next.setVisibility(View.GONE);
                //stepContainer.setBackgroundResource(R.drawable.blue_rounded_new);
                // playArea.setPadding(15, 5, 15, 5);
            }
            if (step.getType().equalsIgnoreCase(StepType.MEMORYGAME)) {
                next.setVisibility(View.GONE);
                // playArea.setPadding(10, 5, 10, 5);
            }
            activateStepper();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200)
        {
            //mbCountDownTimer.startBtn();
            timerStart();
            startChallenge();
        }
    }*/

    @OnActivityResult(RESULT_ADVERTISE)
    void catchOnActivityResult(int resultCode) {
        if (RESULT_OK == resultCode) {

            stepCounter++;
            if (steps.size() > 0 && stepCounter < steps.size()) {

                mbCountDownTimer = new MBCountDownTimer(millisUntilFinished, 1000, this);
                startChallenge();
               // mbCountDownTimer.start();
            }else if (stepCounter == steps.size()) {
                stepCounter--;
                gotoNextStep();
            }
        }
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

        int tempStepCounter = stepCounter;

        for (int i = 0; i < steppers.getChildCount(); i++) {
            View view = steppers.getChildAt(i);
            RelativeLayout tab = (RelativeLayout) view.findViewById(R.id.tab);
            View on = (View) view.findViewById(R.id.on);
            View off = (View) view.findViewById(R.id.off);
            View played = (View) view.findViewById(R.id.played);
            on.setVisibility(View.GONE);
            off.setVisibility(View.GONE);
            played.setVisibility(View.GONE);

            if (tempStepCounter == i) {
                on.setVisibility(View.VISIBLE);
            }

            if (i < tempStepCounter) {
                played.setVisibility(View.VISIBLE);
            }

            if (i > tempStepCounter) {
                off.setVisibility(View.VISIBLE);
            }
        }
    }

    private void quitChallenge() {
        if (feedback1.getVisibility() == View.GONE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.quit_challenge)).setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                ((ApplicationSingleton) getApplication()).isChallengePlaying = false;
                //    if (mp != null)
                //       mp.release();
                if (mbCountDownTimer != null)
                    mbCountDownTimer.cancel();
                finish();
            }).setNegativeButton(getResources().getString(R.string.no), null);
            AlertDialog alert = builder.create();
            alert.show();
            FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
        }
    }

    private void submitOfflineChallenge() {
        steps.set(stepCounter, itemStep.getStep());
        challange.setSteps(steps);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < challange.getSteps().size(); i++) {
                Step s = challange.getSteps().get(i);
                JSONObject stepObj = new JSONObject();
                stepObj.put("stepId", s.getStepKey());
                stepObj.put("last_step", (s.isLastStep()) ? "1" : "0");

                if (s.getType().equalsIgnoreCase(StepType.QUIZ)) {
                    stepObj.put("answerId", s.getStepAnswer());
                } else if (s.getType().equalsIgnoreCase(StepType.POLLING)) {
                    stepObj.put("pollingId", s.getStepAnswer());
                } else if (s.getType().equalsIgnoreCase(StepType.SURVEYMULTISELECT)) {
                    stepObj.put("answerId", Util.convertToCommaSeparated(s.getMultiSelectSurveyAnswers()));
                } else if (s.getType().equalsIgnoreCase(StepType.SURVEY)) {
                    stepObj.put("answer", s.getUserFeedback());
                } else if (s.getType().toLowerCase().contains(StepType.ANAGRAM)) {
                    stepObj.put("answer", s.getUserFeedback());
                } else if (s.getType().equalsIgnoreCase(StepType.RIDDLE)) {
                    stepObj.put("solved", (s.isRiddleSolved()) ? "1" : "0");
                } else if (s.getType().equalsIgnoreCase(StepType.JIGSAW)) {
                    stepObj.put("completed", (s.isPuzzleSolved()) ? "1" : "0");
                } else if (s.getType().equalsIgnoreCase(StepType.MEMORYGAME)) {
                    stepObj.put("completed", (s.isMemoryGameSolved()) ? "1" : "0");

                }

                jsonArray.put(stepObj);
            }
            jsonObject.put("steps", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        challange.setOfflineSavedAnwers(jsonObject.toString());
        challange.setOfflineCompleted(true);
        DbHelper.getInstance(this).saveChallenge(challange, "offline");
        submitChallengeOfflineAlert();
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

    private void saveAllSteps() {
        hideLoader(progress);
        showLoader(progress);
        StartChallenge startChallenge = Util.getStartChallengeDetails(this);
        Call<APIResponse> callback;
        if (step.getType().equalsIgnoreCase(StepType.QUIZ)) {
            callback = apiService.saveStepQuizNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), itemStep.getStep().getStepAnswer(), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.POLLING)) {
            callback = apiService.saveStepPollingNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), itemStep.getStep().getStepAnswer(), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.SURVEYMULTISELECT)) {
            callback = apiService.saveMultiselectSurveyNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), Util.convertToCommaSeparated(step.getMultiSelectSurveyAnswers()), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.SURVEY)) {
            callback = apiService.saveSurveyNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), itemStep.getStep().getUserFeedback(), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().toLowerCase().contains(StepType.ANAGRAM)) {
            callback = apiService.saveAnagramNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), itemStep.getStep().getUserFeedback(), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.RIDDLE)) {
            callback = apiService.saveRiddleNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), (itemStep.getStep().isRiddleSolved()) ? "1" : "0", startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.JIGSAW)) {
            callback = apiService.saveJigsawPuzzleNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), (itemStep.getStep().isPuzzleSolved()) ? "1" : "0", startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else if (step.getType().equalsIgnoreCase(StepType.MEMORYGAME)) {
            callback = apiService.saveMemoryGameNew(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), (itemStep.getStep().isMemoryGameSolved()) ? "1" : "0", startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        } else {
            callback = apiService.saveAllSteps(Constants.MB_API_KEY, user.getAuthToken(), step.getStepKey(), challange.getId(), step.getId(), step.getSlug(), startChallenge.getPlayedId(), (itemStep.getStep().isLastStep()) ? "1" : "0");
        }
        coins = null;
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    //Log.d("new Save Step Response", new Gson().toJson(response.body().getValues()));
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 200) {
                            gotoNextStep();
                        } else if (response.body().getMeta().getStatusCode() == 204) {
                            Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                            finish();
                        } else if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengePlayActivity.this, user);
                                saveAllSteps();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                saveAllSteps();
                Log.d("API Error", Constants.SOME_THING_WRONG);
            }
        });

    }


    private void postChallengeLastResult(String isRight) {
        hideLoader(progress);
        showLoader(progress);
        StartChallenge startChallenge = Util.getStartChallengeDetails(this);
        Call<APIResponse<RafflesResponseModel>> callback;
        callback = apiService.postChallengeLastResult(Constants.MB_API_KEY, user.getAuthToken(), challange.getId(), isRight);
        callback.enqueue(new Callback<APIResponse<RafflesResponseModel>>() {
            @Override
            public void onResponse(Response<APIResponse<RafflesResponseModel>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getValues() != null) {


                            screenAfterCompletingRaffle(response.body().getMeta().getMessage(), response.body().getValues());


                        } else if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengePlayActivity.this, user);
                                postChallengeLastResult(isRight);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                       // Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                saveAllSteps();
                Log.d("API Error", Constants.SOME_THING_WRONG);
            }
        });

    }
/*
    private void postPlayStep() {
        hideLoader(progress);
        showLoader(progress);
        StartChallenge startChallenge = Util.getStartChallengeDetails(this);
        Call<APIResponse> callback;
        callback = apiService.postPlayStep(Constants.MB_API_KEY, user.getAuthToken(), challange.getId(), startChallenge.getPlayedId(), totalPoints);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getValues() != null) {

                        } else if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengePlayActivity.this, user);
                                postPlayStep();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(ChallengePlayActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                saveAllSteps();
                Log.d("API Error", Constants.SOME_THING_WRONG);
            }
        });

    }
*/

    /*private void challengeRating() {

        int rating = Math.round(ratingBar.getRating());
        if (rating > 5)
            rating = 5;

        Call<APIResponse> callback = apiService.challengeRating(Constants.MB_API_KEY, user.getAuthToken(), challange.getSlug(), rating + "");
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader();
                Log.d("Save Step Response", new Gson().toJson(response.body()));
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 200) {

                    } else {
                        ratingBar.setEnabled(true);
                    }

                } else {
                    ratingBar.setEnabled(true);
                    Util.makeToast(ChallengePlayActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ratingBar.setEnabled(true);
                hideLoader();
            }
        });
    }*/

    private void setBackgrounImage(String imageUrl) {
        String filePath = "";

        if(imageUrl != null && !imageUrl.trim().isEmpty())
        {
            filePath = imageUrl;
        }else
        {
            filePath = challange.getStartBackgroundImage();
        }


        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = challange.getStartBackgroundImage().substring(challange.getStartBackgroundImage().lastIndexOf('/') + 1, challange.getStartBackgroundImage().length());
                String savedFile = Constants.FOLDER_PATH(challange.getSlug(), fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    background.setImageURI(imageURI);
                } else {
                    // Util.makeToast(context, "Image Not found");
                }
            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(background, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        }
    }

    private void setEndBackgroundImage() {
        String filePath = challange.getEndBackgroundImage();
        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = challange.getEndBackgroundImage().substring(challange.getEndBackgroundImage().lastIndexOf('/') + 1, challange.getEndBackgroundImage().length());
                String savedFile = Constants.FOLDER_PATH(challange.getSlug(), fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    feedback_background.setImageURI(imageURI);
                } else {
                    // Util.makeToast(context, "Image Not found");
                }
            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(feedback_background, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        }
    }

    public void onKeyClick(View v) {
        String letter = ((TextView) v).getText().toString();
        itemStep.validateRiddle(letter);
    }

    @Click(R.id.facebook_share)
    void onFacebookShare() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(Constants.WEB_BASE_URL + "/challenge/details/" + challange.getSlug()))
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if(slug!=null){
            getLiveChallengeDetail();
        }
*/
        /*AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
        if (ad != null)
            pointsNum.setText(Integer.toString(ad.getCoinBalance()));*/

        ((ApplicationSingleton) getApplication()).isChallengePlaying = true;
        if (!offline) {
            if (!Connectivity.isConnected(this)) {
                noNetworkDialog();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ApplicationSingleton) getApplication()).isChallengePlaying = false;
        //Log.e("Challenge play activity", "onDestroy");
        //  if (mp != null)
        //     mp.release();
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
        if (!hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                appAlert();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_EXTERNAL_STORAGE);
            }
        } else {
            saveScreenShot();
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.recharge_perm_app_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(ChallengePlayActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_EXTERNAL_STORAGE);
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
            case MY_PERMISSION_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveScreenShot();
                } else {
                    Util.makeToast(ChallengePlayActivity.this, getResources().getString(R.string.recharge_perm_toast));
                }
                return;
            }
        }
    }
}
