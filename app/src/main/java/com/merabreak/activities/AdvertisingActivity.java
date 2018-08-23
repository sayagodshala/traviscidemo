package com.merabreak.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.StepType;
import com.merabreak.Util;
import com.merabreak.controls.AdvertisingStep;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Step;
import com.merabreak.utils.ScalableVideoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static java.security.AccessController.getContext;


@EActivity(R.layout.activity_step_advertisement)
public class AdvertisingActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, MediaPlayer.OnCompletionListener {

    private static String TAG = AdvertisingStep.class.getName();
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    LayoutInflater layoutInflater;
    private Context context;

    int skipTime = 5;
    int skipTimeForVideo = 10;

    @Extra
    Step step;

    @Extra
    String challengeSlug;

    @Extra
    String contentLanguage;

    @Extra
    boolean offline;

    private ImageLoader imageLoader;
    String youtubeVideoId = "";
    private YouTubePlayer youTubePlayer = null;

    @ViewById(R.id.ads_image)
    ImageView adsImageView;

    @ViewById(R.id.ads_youtube_view)
    YouTubePlayerView adsYoutubeView;

    @ViewById(R.id.ads_video)
    FullscreenVideoLayout adsVideoFrameView;

    @ViewById(R.id.ads_videoView)
    VideoView adsVideoView;

    @ViewById(R.id.step_container)
    LinearLayout stepContainer;

    @ViewById(R.id.skip_btn)
    Button skipBtn;

    @ViewById(R.id.skip_img_btn)
    ImageButton skipImgBtn;

    @ViewById(R.id.skip_hldr)
    RelativeLayout skipHldr;

    boolean isAdsRunning;

    @ViewById(R.id.progressbar)
    ProgressBar progressbar;

    @ViewById(R.id.progressbar2)
    ProgressBar progressbar2;
    private MyCountDownTimer myCountDownTimer;

    private int seekTime;

    @AfterViews
    void init() {

        imageLoader = new ImageLoader(this);

        switch (step.getSlug().toLowerCase()) {
            case StepType.ADURI:

                displayAdvertise();
                break;
            case StepType.INTERSTITIAL_ADS:

                displayAdvertise();

                break;
        }
        skipImgBtn.setEnabled(false);
        skipImgBtn.setAlpha(0.5f);

        skipHldr.setEnabled(false);

        /*switch (step.getType().toLowerCase()) {
            case StepType.ADURI:

                setAdUrl();
                break;
        }*/

        /*AdvertisingStep advertisingStep = AdvertisingStep_.build(this);
        advertisingStep.init(step, this, offline, challengeSlug, contentLanguage);
        stepContainer.addView(advertisingStep);*/
    }

    private void setSkipTimeHorizontal(int duration)
    {
        progressbar2.setProgress(100);
        myCountDownTimer = new MyCountDownTimer(duration,100);
        myCountDownTimer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isAdsRunning = false;
        onBackPressed();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/100);
            progressbar2.setProgress(progress);
        }

        @Override
        public void onFinish() {
            progressbar2.setProgress(0);
        }

    }
    private void setSkipTimeProgress(ProgressBar pb,int duration)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 0, 100);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                //do something when the countdown is complete
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        animation.start();
    }

    private void displayAdvertise()
    {
        if(step.getSlug().toLowerCase().equals(StepType.ADURI))
        {
            if(!Util.textIsEmpty(step.getImage_url()))
            {
                adsImageView.setVisibility(View.VISIBLE);

                //imageLoader.DisplayImage(step.getImage_url(), adsImageView);
                Picasso.with(context).load(step.getImage_url()).into(adsImageView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if(step.getSkip_time() != 0) {

                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(step.getSkip_time());
                    skipTimeForAdvertising(step.getSkip_time() * 1000L);
                }
                else {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(skipTime);
                    skipTimeForAdvertising(skipTime * 1000L);
                }

            }else if(!Util.textIsEmpty(step.getVideo_url()))
            {
                // play adsVideoFrameView

                Uri videoUri = Uri.parse(step.getVideo_url());
                playVideoInVideoView(videoUri);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                /*adsVideoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));*/



                // set Skip Time

                if(step.getSkip_time() != 0) {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(step.getSkip_time());
                    skipTimeForAdvertising(step.getSkip_time() * 1000L);
                }
                else {
                    skipHldr.setVisibility(View.GONE);
                }

                /*adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = videoUri.getQueryParameter("v");
                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                adsYoutubeView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));*/




                // play

                // play adsVideoFrameView in framesVideoView
                /*
                    adsYoutubeView.setVisibility(View.GONE);
                    adsVideoFrameView.setVisibility(View.VISIBLE);
                    try {

                        adsVideoFrameView.setVideoURI(videoUri);
                        adsVideoFrameView.requestFocus();
                        adsVideoFrameView.start();
                        *//*adsVideoFrameView.hideControls();
                        adsVideoFrameView.seekTo(1);*//*
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/



            }
        }else if (step.getSlug().toLowerCase().equals(StepType.INTERSTITIAL_ADS))
        {
            if(!Util.textIsEmpty(step.getImage_url()) && !Util.textIsEmpty(step.getVideo_url()))
            {
                adsImageView.setVisibility(View.VISIBLE);
                //imageLoader.DisplayImage(step.getImage_url(), adsImageView);
                Picasso.with(context).load(step.getImage_url()).into(adsImageView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });

                Uri videoUri = Uri.parse(step.getVideo_url());


                playVideoInVideoView(videoUri);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if(!Util.textIsEmpty(step.getPosition()))
                {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) adsVideoView.getLayoutParams();
                    if(step.getPosition().toLowerCase().equals("bottom"))
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }
                    else if(step.getPosition().toLowerCase().equals("top"))
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    }
                    else {
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                }

                // set Skip time
                if(step.getSkip_time() != 0) {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(step.getSkip_time());
                    skipTimeForAdvertising(step.getSkip_time() * 1000L);
                }
                else {
                    skipHldr.setVisibility(View.GONE);
                }

                /*adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = videoUri.getQueryParameter("v");
                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);


                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if(!Util.textIsEmpty(step.getPosition()))
                {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)adsYoutubeView.getLayoutParams();
                    if(step.getPosition().toLowerCase().equals("bottom"))
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }
                    else if(step.getPosition().toLowerCase().equals("top"))
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    }
                    else {
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    }
                }*/



            }
            else if(!Util.textIsEmpty(step.getImage_url()))
            {
                adsImageView.setVisibility(View.VISIBLE);
                //imageLoader.DisplayImage(step.getImage_url(), adsImageView);
                Picasso.with(context).load(step.getImage_url()).into(adsImageView, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                if(step.getSkip_time() != 0) {
                    //setSkipTimeHorizontal(step.getSkip_time() * 1000);
                    //setSkipTimeProgress(progressbar, step.getSkip_time() * 1000);
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(step.getSkip_time());
                    skipTimeForAdvertising(step.getSkip_time() * 1000L);
                }
                else {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(skipTime);
                    skipTimeForAdvertising(skipTime * 1000L);
                }

            }else if(!Util.textIsEmpty(step.getVideo_url()))
            {
                Uri videoUri = Uri.parse(step.getVideo_url());

                playVideoInVideoView(videoUri);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                /*adsVideoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));*/

               /* adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = videoUri.getQueryParameter("v");

                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                adsYoutubeView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));*/


                if(step.getSkip_time() != 0) {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(step.getSkip_time());
                    skipTimeForAdvertising(step.getSkip_time() * 1000L);
                }
                else {
                    skipHldr.setVisibility(View.VISIBLE);
                    setProgressWheel(skipTimeForVideo);
                    skipTimeForAdvertising(skipTimeForVideo * 1000L);
                }

            }else
                onBackPressed();
        }


    }

    void playVideoInVideoView(Uri videoUri)
    {
        adsVideoFrameView.setVisibility(View.GONE);
        adsYoutubeView.setVisibility(View.GONE);
        adsVideoView.setOnCompletionListener(this);
        adsVideoView.setVisibility(View.VISIBLE);
        adsVideoView.setVideoURI(videoUri);
        adsVideoView.requestFocus();
        adsVideoView.start();

    }

@ViewById(R.id.view_progress_bar)
    ProgressBar progressBarView;
    private void setProgressWheel(int endTime)
    {

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);


        fn_countdown(endTime);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        this.youTubePlayer = youTubePlayer;
        //  youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the adsVideoFrameView player.
        //  youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        //   youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        youTubePlayer.setPlayerStateChangeListener(this);

        if (youtubeVideoId != null) {
            if (b) {
                youTubePlayer.play();
            } else {
                youTubePlayer.loadVideo(youtubeVideoId);
                //youTubePlayer.loadVideo("_oEA18Y8gM0");
                //youTubePlayer.play();
            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog((Activity) context, RECOVERY_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.item_step_error_load_video), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        if(!isAdsRunning && !adsVideoView.isPlaying()) {
            setResult(RESULT_OK);
            super.onBackPressed();
        }
       // setResult(RESULT_OK,new Intent(this,ChallengePlayActivity.class));
    }

    @Background
    void skipTimeForAdvertising(long milliSeconds) {
        try {
            isAdsRunning = true;
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        backToChallenge();
    }

    @UiThread
    void backToChallenge(){

        isAdsRunning = false;
        skipImgBtn.setEnabled(true);
        skipImgBtn.setAlpha(1f);

        skipHldr.setEnabled(true);
      //  skipBtn.setVisibility(View.VISIBLE);
    }

    @Click(R.id.skip_btn)
    void skipBtnClick()
    {
        onBackPressed();
    }

    @Click(R.id.skip_img_btn)
    void setSkipImgBtnClick()
    {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Click(R.id.skip_hldr)
    void setSkipHolderClick()
    {
        onBackPressed();
    }


    @Override
    public void onLoading() {
        isAdsRunning = true;
    }

    @Override
    public void onLoaded(String s) {
        isAdsRunning = true;
    }

    @Override
    public void onAdStarted() {
        isAdsRunning = true;
    }

    @Override
    public void onVideoStarted() {
        isAdsRunning = true;
    }

    @Override
    public void onVideoEnded() {

        isAdsRunning = false;
        if(!isAdsRunning) {
            onBackPressed();
        }

    }


    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    int myProgress = 0;

    int progress;
    CountDownTimer countDownTimer;
    int endTime = 250;


    private void fn_countdown(int endTime) {

            myProgress = 0;

            try {
                countDownTimer.cancel();

            } catch (Exception e) {

            }

            //String timeInterval = et_timer.getText().toString();
            progress = 1;
            //endTime = Integer.parseInt(timeInterval); // up to finish time

            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime);
                    progress = progress + 1;
                    /*int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    String newtime = hours + ":" + minutes + ":" + seconds;

                    if (newtime.equals("0:0:0")) {
                        tv_time.setText("00:00:00");
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(hours).length() == 1) {
                        tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        tv_time.setText(hours + ":0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_time.setText(hours + ":" + minutes + ":0" + seconds);
                    } else {
                        tv_time.setText(hours + ":" + minutes + ":" + seconds);
                    }*/

                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime);


                }
            };
            countDownTimer.start();


    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    @Override
    protected void onPause() {
        super.onPause();
        seekTime = adsVideoView.getCurrentPosition();
        adsVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adsVideoView.resume();
        adsVideoView.seekTo(seekTime);
        adsVideoView.start();
    }
}
