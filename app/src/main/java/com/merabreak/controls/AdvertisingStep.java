package com.merabreak.controls;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.StepType;
import com.merabreak.Util;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Step;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

@EViewGroup(R.layout.item_step_advertising)
public class AdvertisingStep extends RelativeLayout implements YouTubePlayer.OnInitializedListener {

    private static String TAG = AdvertisingStep.class.getName();
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    LayoutInflater layoutInflater;
    private Context context;
    private Step step;
    private String challengeSlug;
    private String contentLanguage;
    private boolean offline;
    private ImageLoader imageLoader;
    String youtubeVideoId = "";
    private YouTubePlayer youTubePlayer = null;

    @ViewById(R.id.ads_image)
    ImageView adsImageView;

    @ViewById(R.id.ads_youtube_view)
    YouTubePlayerView adsYoutubeView;

    @ViewById(R.id.video)
    FullscreenVideoLayout video;


    public AdvertisingStep(Context context) {
        super(context);
    }

    public void init(Step step, Context context, boolean offline, String challengeSlug, String contentLanguage) {

        imageLoader = new ImageLoader(context);
        this.step = step;
        this.context = context;
        this.challengeSlug = challengeSlug;
        this.contentLanguage = contentLanguage;

        //adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);
        setAdUrl();

        /*switch (step.getType().toLowerCase()) {
            case StepType.ADURI:

                displayAdvertise();
                break;
            case StepType.INTERSTITIAL_ADS:

                displayAdvertise();

                //setAdUrl();
                break;
        }
*/
    }

    private void displayAdvertise()
    {
        if(step.getSlug().toLowerCase().equals(StepType.ADURI))
        {
            if(!Util.textIsEmpty(step.getImage_url()))
            {
                adsImageView.setVisibility(VISIBLE);
                imageLoader.DisplayImage(step.getImage_url(), adsImageView);
            }else if(!Util.textIsEmpty(step.getVideo_url()))
            {
                adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = step.getVideo_url();
                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);
            }
        }else if (step.getSlug().toLowerCase().equals(StepType.INTERSTITIAL_ADS))
        {
            if(!Util.textIsEmpty(step.getImage_url()) && !Util.textIsEmpty(step.getVideo_url()))
            {
                adsImageView.setVisibility(VISIBLE);
                imageLoader.DisplayImage(step.getImage_url(), adsImageView);

                adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = step.getVideo_url();
                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);
            }
        }
    }

    private void setAdUrl() {

        if (step.getAdUrl().contains("http://res.cloudinary.com/merabreak-media/image/upload/v1/advertisement/")) {
            step.setUserCompleted(true);

            if (offline) {
                String filePath = step.getAdUrl();
                if (filePath != null && filePath.contains("http")) {
                    String fileName = filePath.substring(step.getAdUrl().lastIndexOf('/') + 1, step.getAdUrl().length());
                    String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                    File file = new File(savedFile);
                    if (file.exists()) {
                        Uri imageURI = Uri.parse(savedFile);
                        //Log.d("Downloaded IMGURI", savedFile);
                        adsImageView.setImageURI(imageURI);
                    } else {
                        Util.makeToast(context, context.getResources().getString(R.string.item_step_image_not_found));
                    }
                }
            } else {
                if (step.getAdUrl() != null && !step.getAdUrl().equalsIgnoreCase("")) {
                    imageLoader.DisplayImage(step.getAdUrl(), adsImageView);
                }
            }
        } else {
            Uri videoUri = Uri.parse(step.getAdUrl());
            step.setUserCompleted(true);
            if (videoUri.getHost().contains("youtube")) {
                //Log.d("first video", "first video");

                step.setUserCompleted(true);
                adsYoutubeView.setVisibility(View.VISIBLE);
                youtubeVideoId = videoUri.getQueryParameter("v");
                adsYoutubeView.initialize(Constants.GOOGLE_API_KEY, this);
            } else {
                //Log.d("second video", "second video");
                video.setVisibility(View.VISIBLE);
                //video.setFullscreen(false);
                if (offline) {
                    if (step.getAdUrl() != null && step.getAdUrl().contains("http://")) {
                        String fileName = step.getAdUrl().substring(step.getAdUrl().lastIndexOf('/') + 1, step.getAdUrl().length());
                        String savedFile = Constants.FOLDER_PATH(challengeSlug, fileName);
                        File file = new File(savedFile);
                        if (file.exists()) {
                            videoUri = Uri.parse(savedFile);
                            try {
                                video.setVideoURI(videoUri);
                                //Log.d("Downloaded Video", Constants.FOLDER_PATH(challengeSlug, fileName));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.makeToast(context, context.getResources().getString(R.string.item_step_video_not_found));
                        }
                    }
                } else {
                    //Log.d("third video", "third video");
                    if (step.getAdUrl() != null && step.getAdUrl().contains("http://")) {
                        videoUri = Uri.parse(step.getAdUrl());
                        try {
                            video.setVideoURI(videoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        this.youTubePlayer = youTubePlayer;
          youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        // youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        //   youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

        if (youtubeVideoId != null) {
            if (b) {
                youTubePlayer.play();
            } else {
                youTubePlayer.loadVideo(youtubeVideoId);
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
}
