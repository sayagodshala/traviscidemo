package com.merabreak.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivityForYoutube;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.GSAdLink;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.challenge.Article;
import com.merabreak.models.challenge.Astrology;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.Connectivity;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EActivity(R.layout.article_layout)
public class ArticleActivity extends BaseActivityForYoutube implements YouTubePlayer.OnInitializedListener {

    @ViewById(R.id.mb_gs_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.article_content)
    LinearLayout articleContent;

    @ViewById(R.id.dynamic_content)
    LinearLayout dynamicContent;

    @ViewById(R.id.article_submit)
    Button articleSubmit;

    @ViewById(R.id.background)
    ImageView backImage;

    @ViewById(R.id.astrology_bottom_ad)
    ImageView astrologyBottomAd;

    @ViewById(R.id.astrology_bottom_view)
    View astrologyEmptyView;

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    TextView retry;

    List<Article> articles;
    List<Step> steps;
    Step step;
    Article article;

    private YouTubePlayer youTubePlayer = null;
    String youtubeVideoId = "";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    LayoutInflater layoutInflater;
    long startTime, endTime, spendTime;
    String totSpendTime, mbGSAdLink, mbGsAdLinkName;
    int spendTimeSec;

    @Extra
    Challenge challenge;

    @Extra
    Astrology astrology;

    @AfterViews
    void init() {
        /*toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitArticleAlert();
            }
        });*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startTime = System.currentTimeMillis();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*if (android.os.Build.VERSION.SDK_INT >= 21)
            Util.changeSystemBarColor(this, R.color.transparent);*/

        if (challenge != null) {
            //toolbar.setVisibility(View.GONE);
            String filePath = challenge.getBackgroundImage();
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(backImage, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
            getChallengeDetails();
        } else {
            articleSubmit.setText("Got It");
            String filePath = astrology.getBackgroundImage();
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(backImage, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
            getAstrologyDetails();
        }
    }

    private void setProblem(String errorString, String tag) {
        problem.setVisibility(View.VISIBLE);
        error.setText(errorString);
        error.setTag(tag);
        retry.setText(R.string.retry_text);
    }

    @Click(R.id.retry)
    void onRetryClick() {
        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("2")) {
                problem.setVisibility(View.GONE);
                if (challenge != null)
                    getChallengeDetails();
                else
                    getAstrologyDetails();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                problem.setVisibility(View.GONE);
                if (challenge != null)
                    getChallengeDetails();
                else
                    getAstrologyDetails();
            }
        }
    }

    private void getChallengeDetails() {
        hideLoader(progress);
        showLoader(progress);

        Call<APIResponse<Challenge>> callback = apiService.getChallengeDetail(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug(), Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<Challenge>>() {
            @Override
            public void onResponse(Response<APIResponse<Challenge>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ArticleActivity.this, user);
                                getChallengeDetails();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                //Log.d("Challenge Data", new Gson().toJson(response.body().getValues()));
                                if (response.body().getValues() != null) {
                                    challenge = response.body().getValues();
                                    setData();
                                } else {
                                    if (Connectivity.isConnected(ArticleActivity.this))
                                        setProblem(response.body().getMeta().getMessage(), "3");
                                    else
                                        setProblem(context.getResources().getString(R.string.no_internet), "3");
                                }
                            } else {
                                if (Connectivity.isConnected(ArticleActivity.this))
                                    setProblem(response.body().getMeta().getMessage(), "3");
                                else
                                    setProblem(context.getResources().getString(R.string.no_internet), "3");
                            }
                        }
                    } else {
                        if (Connectivity.isConnected(ArticleActivity.this))
                            setProblem(Constants.SOME_THING_WRONG, "2");
                        else
                            setProblem(context.getResources().getString(R.string.no_internet), "2");
                    }
                } else {
                    if (Connectivity.isConnected(ArticleActivity.this))
                        setProblem(Constants.SOME_THING_WRONG, "2");
                    else
                        setProblem(context.getResources().getString(R.string.no_internet), "2");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (Connectivity.isConnected(ArticleActivity.this))
                    setProblem(Constants.SOME_THING_WRONG, "2");
                else
                    setProblem(context.getResources().getString(R.string.no_internet), "2");

            }
        });
    }

    private void getAstrologyDetails() {
        hideLoader(progress);
        showLoader(progress);

        Call<APIResponse<Astrology>> callback = apiService.getGSAstrologyDetails(Constants.MB_API_KEY, user.getAuthToken(), astrology.getId(), Util.getDeviceDPI(this));
        callback.enqueue(new Callback<APIResponse<Astrology>>() {
            @Override
            public void onResponse(Response<APIResponse<Astrology>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ArticleActivity.this, user);
                                getChallengeDetails();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                //Log.d("Challenge Data", new Gson().toJson(response.body().getValues()));
                                if (response.body().getValues() != null) {
                                    astrology = response.body().getValues();
                                    title.setText(astrology.getTitle());
                                    setData();
                                } else {
                                    if (Connectivity.isConnected(ArticleActivity.this))
                                        setProblem(response.body().getMeta().getMessage(), "3");
                                    else
                                        setProblem(context.getResources().getString(R.string.no_internet), "3");
                                }
                            } else {
                                if (Connectivity.isConnected(ArticleActivity.this))
                                    setProblem(response.body().getMeta().getMessage(), "3");
                                else
                                    setProblem(context.getResources().getString(R.string.no_internet), "3");
                            }
                        }
                    } else {
                        if (Connectivity.isConnected(ArticleActivity.this))
                            setProblem(Constants.SOME_THING_WRONG, "2");
                        else
                            setProblem(context.getResources().getString(R.string.no_internet), "2");
                    }
                } else {
                    if (Connectivity.isConnected(ArticleActivity.this))
                        setProblem(Constants.SOME_THING_WRONG, "2");
                    else
                        setProblem(context.getResources().getString(R.string.no_internet), "2");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (Connectivity.isConnected(ArticleActivity.this))
                    setProblem(Constants.SOME_THING_WRONG, "2");
                else
                    setProblem(context.getResources().getString(R.string.no_internet), "2");

            }
        });
    }

    @Click(R.id.article_submit)
    void articleDone() {
        if (Connectivity.isConnected(this)) {
            if (challenge != null && challenge.getSteps().size() > 1) {
                endTime = System.currentTimeMillis();
                spendTime = endTime - startTime;
                totSpendTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spendTime),
                        TimeUnit.MILLISECONDS.toMinutes(spendTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(spendTime) % TimeUnit.MINUTES.toSeconds(1),
                        spendTime % 1000);
                articleUpdate(totSpendTime);
            } else {
               // exitArticleAlert();
                if (Connectivity.isConnected(this)) {
                    endTime = System.currentTimeMillis();
                    spendTime = endTime - startTime;
                    totSpendTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spendTime),
                            TimeUnit.MILLISECONDS.toMinutes(spendTime) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(spendTime) % TimeUnit.MINUTES.toSeconds(1),
                            spendTime % 1000);
                    if (challenge != null) {
                        articleUpdate(totSpendTime);
                    } else {
                        astrologyUpdate(totSpendTime);
                    }
                } else {
                    Util.makeToast(this, context.getResources().getString(R.string.no_internet));
                    finish();
                }
            }
        } else {
            Util.makeToast(this, context.getResources().getString(R.string.no_internet));
            finish();
        }
    }

    @Click(R.id.astrology_bottom_ad)
    void astrologyAd() {
        getZodiacAdLink(astrology.getId(), 0);
    }

    private void setData() {
        if (challenge != null) {
            if (challenge.getSteps().size() > 1)
                articleSubmit.setText("Next");
            articles = challenge.getSteps().get(0).getArticles();
        } else {
            GoogleAnalyticEvents.event(context, gaTracker, "GaneshaSpeaks.com - Horoscope", "User Clicked On " + astrology.getTitle(), user.getFullName() + ": " + user.getPhone());
            String filePath = astrology.getAdImage();
            if (filePath != null && filePath.contains("http") && !filePath.equalsIgnoreCase("")) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(355, 100);
                astrologyBottomAd.setVisibility(View.VISIBLE);
                astrologyEmptyView.setVisibility(View.VISIBLE);
                params.setMargins(0, 50, 0, 240);
                params.gravity = Gravity.CENTER;
                articleSubmit.setLayoutParams(params);
                Picasso.with(context).load(filePath).into(astrologyBottomAd, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
            articles = astrology.getArticles();
        }
        if (articles.size() > 0) {
            articleContent.setVisibility(View.VISIBLE);
            for (int i = 0; i < articles.size(); i++) {
                if (articles.get(i).getContent_type().equalsIgnoreCase("header1")) {
                    View answerView = layoutInflater.inflate(R.layout.item_article_header, null);
                    final TextView header1 = (TextView) answerView.findViewById(R.id.header);
                    header1.setText(articles.get(i).getContent());
                    dynamicContent.addView(answerView);
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("header2")) {
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
                    View answerView = layoutInflater.inflate(R.layout.item_article_sub_header, null);
                    final TextView sub_header = (TextView) answerView.findViewById(R.id.sub_header);
                    sub_header.setTypeface(font);
                    sub_header.setText(articles.get(i).getContent());
                    dynamicContent.addView(answerView);
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("text")) {
                    View answerView = layoutInflater.inflate(R.layout.item_article_text, null);
                    final TextView text = (TextView) answerView.findViewById(R.id.text);
                    text.setText(articles.get(i).getContent());
                    dynamicContent.addView(answerView);
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("quote")) {
                    View answerView = layoutInflater.inflate(R.layout.item_article_quotation, null);
                    final TextView quote = (TextView) answerView.findViewById(R.id.quotation);
                    // SpannableStringBuilder ssb = new SpannableStringBuilder(articles.get(i).getContent());
                    //  ssb.setSpan(new ImageSpan(context, R.drawable.start_quote_icon), R.drawable.start_quote_icon, R.drawable.start_quote_icon, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    //   quote.setText(ssb, TextView.BufferType.SPANNABLE);
                    quote.setText(articles.get(i).getContent());
                    dynamicContent.addView(answerView);
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("link")) {
                    View answerView = layoutInflater.inflate(R.layout.mb_gs_link, null);
                    final TextView text = (TextView) answerView.findViewById(R.id.link);
                    text.setText(Html.fromHtml("<u>" + articles.get(i).getContent() + "</u>"));
                    text.setClickable(false);
                    text.setFocusable(false);
                    int contentId = articles.get(i).getContent_id();
                    dynamicContent.addView(answerView);
                    if (astrology != null) {
                        text.setClickable(true);
                        text.setFocusable(true);
                        text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //GoogleAnalyticEvents.event(ArticleActivity.this, gaTracker, "GaneshaSpeaks.com", "User Clicked Astrology Link", user.getFullName() + ": " + user.getPhone());
                                getZodiacAdLink(0, contentId);
                            }
                        });
                    }
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("image")) {
                    View answerView = layoutInflater.inflate(R.layout.item_article_image, null);
                    final ImageView image = (ImageView) answerView.findViewById(R.id.article_image);
                    String filePath = articles.get(i).getContent();
                    if (filePath != null && filePath.contains("http")) {
                        Picasso.with(context).load(filePath).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                            }
                        });
                    }
                    image.setClickable(false);
                    image.setFocusable(false);
                    int contentId = articles.get(i).getContent_id();
                    dynamicContent.addView(answerView);
                    if (astrology != null) {
                        image.setClickable(true);
                        image.setFocusable(true);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //GoogleAnalyticEvents.event(ArticleActivity.this, gaTracker, "GaneshaSpeaks.com", "User Clicked Astrology Ad", user.getFullName() + ": " + user.getPhone());
                                getZodiacAdLink(0, contentId);
                            }
                        });
                    }
                } else if (articles.get(i).getContent_type().equalsIgnoreCase("adsVideoFrameView")) {
                    Uri videoUri = Uri.parse(articles.get(i).getContent());
                    View answerView = layoutInflater.inflate(R.layout.item_article_video, null);
                    if (videoUri.getHost().contains("youtube")) {
                        final YouTubePlayerView youtubeVideo = (YouTubePlayerView) answerView.findViewById(R.id.articleYouTubePlayerView);
                        youtubeVideo.setVisibility(View.VISIBLE);
                        youtubeVideoId = videoUri.getQueryParameter("v");
                        youtubeVideo.initialize(Constants.GOOGLE_API_KEY, this);
                    } else {
                        final FullscreenVideoLayout normalVideo = (FullscreenVideoLayout) answerView.findViewById(R.id.articleVideo);
                        normalVideo.setVisibility(View.VISIBLE);
                        if (articles.get(i).getContent() != null && articles.get(i).getContent().contains("http://")) {
                            videoUri = Uri.parse(step.getAdUrl());
                            try {
                                normalVideo.setVideoURI(videoUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    dynamicContent.addView(answerView);
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
        //  youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the adsVideoFrameView player.
        //   youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        //  youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

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

    @Override
    public void onBackPressed() {
        //Util.makeToast(context, "click on thank you to exit from here");
        if (challenge != null && challenge.getSteps().size() > 1)
            quitChallenge();
        else
            exitArticleAlert();
    }

    private void articleUpdate(String spendTime) {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.articleUpdate(Constants.MB_API_KEY, user.getAuthToken(), challenge.getId(), articles.get(0).getArticle_key(), spendTime);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (challenge.getSteps().size() > 1) {
                                challenge.getSteps().remove(0);
                                startChallenge();
                            } else {
                                finish();
                            }
                        } else {
                            Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void articleExit(String spendTime) {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.articleUpdate(Constants.MB_API_KEY, user.getAuthToken(), challenge.getId(), articles.get(0).getArticle_key(), spendTime);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            finish();
                        } else {
                            Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void exitArticleAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context.getResources().getString(R.string.article_activity_exit_article)).setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
            if (Connectivity.isConnected(this)) {
                endTime = System.currentTimeMillis();
                spendTime = endTime - startTime;
                totSpendTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spendTime),
                        TimeUnit.MILLISECONDS.toMinutes(spendTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(spendTime) % TimeUnit.MINUTES.toSeconds(1),
                        spendTime % 1000);
                if (challenge != null) {
                    articleUpdate(totSpendTime);
                } else {
                    astrologyUpdate(totSpendTime);
                }
            } else {
                Util.makeToast(this, context.getResources().getString(R.string.no_internet));
                finish();
            }
        }).setNegativeButton(context.getResources().getString(R.string.no), null); //Do nothing on no
        AlertDialog alert = builder.create();
        alert.show();
        FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    private void quitChallenge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.quit_challenge)).setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
            if (Connectivity.isConnected(this)) {
                endTime = System.currentTimeMillis();
                spendTime = endTime - startTime;
                totSpendTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spendTime),
                        TimeUnit.MILLISECONDS.toMinutes(spendTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(spendTime) % TimeUnit.MINUTES.toSeconds(1),
                        spendTime % 1000);
                articleExit(totSpendTime);
            } else {
                Util.makeToast(this, context.getResources().getString(R.string.no_internet));
                finish();
            }
        }).setNegativeButton(getResources().getString(R.string.no), null);
        AlertDialog alert = builder.create();
        alert.show();
        FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    private void getZodiacAdLink(int astrologyId, int contentId) {
        Call<APIResponse<GSAdLink>> callback = apiService.getZodiacAdLink(Constants.MB_API_KEY, user.getAuthToken(), astrologyId, contentId);
        callback.enqueue(new Callback<APIResponse<GSAdLink>>() {
            @Override
            public void onResponse(Response<APIResponse<GSAdLink>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ArticleActivity.this, user);
                                getZodiacAdLink(astrologyId, contentId);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    mbGSAdLink = response.body().getValues().getGSAdLink();
                                    mbGsAdLinkName = response.body().getValues().getGSAdLinkName();
                                    if (mbGSAdLink != null && !mbGSAdLink.equalsIgnoreCase("")) {
                                        /*Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(gsAdLink));
                                        startActivity(i);*/

                                        InAppBrowserActivity_.intent(ArticleActivity.this).url(mbGSAdLink).toolbarTitle(getResources().getString(R.string.ganesha_speaks_title)).start();

                                        GoogleAnalyticEvents.event(ArticleActivity.this, gaTracker, "GaneshaSpeaks.com -  Horoscope", "User Clicked On " + mbGsAdLinkName, user.getFullName() + ": " + user.getPhone());
                                    }
                                } else {
                                    Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void astrologyUpdate(String spendTime) {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.postMBGSCount(Constants.MB_API_KEY, user.getAuthToken(), astrology.getId(), spendTime);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            finish();
                        } else {
                            Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(ArticleActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void startChallenge() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<StartChallenge>> callback = apiService.setChallengeStart(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug());
        callback.enqueue(new Callback<APIResponse<StartChallenge>>() {
                             @Override
                             public void onResponse(Response<APIResponse<StartChallenge>> response) {
                                 hideLoader(progress);
                                 if (response.code() == 200 && response.body() != null) {
                                     if (response.body().getMeta() != null) {
                                         if (response.body().getMeta().getStatusCode() == 200) {
                                             if (response.body().getMeta().isStatus()) {
                                                 //Log.d("strat challenge 200", new Gson().toJson(response.body().getValues()));
                                                 Util.saveStartChallengeDetails(ArticleActivity.this, response.body().getValues());
                                                 ((ApplicationSingleton) getApplication()).isChallengePlaying = true;
                                                 ChallengePlayActivity_.intent(ArticleActivity.this).challange(challenge).offline(false).flags(WindowManager.LayoutParams.FLAG_FULLSCREEN).start();
                                                 finish();
                                             } else {
                                                 oops(response.body().getMeta().getMessage());
                                             }
                                         } else if (response.body().getMeta().getStatusCode() == 401) {
                                             if (response.body().getMeta().isStatus()) {
                                                 user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                                 Util.setUser(ArticleActivity.this, user);
                                                 startChallenge();
                                             } else {
                                                 oopsLogout(response.body().getMeta().getMessage());
                                             }
                                         } else {
                                             if (response.body().getMeta().getStatusCode() == 205) {
                                                 Util.makeToast(ArticleActivity.this, response.body().getMeta().getMessage());
                                                 //Log.d("startBtn challenge 205", new Gson().toJson(response.body().getValues()));
                                                 Util.saveStartChallengeDetails(ArticleActivity.this, response.body().getValues());
                                                 // MainActivity_.intent(ChallengeDetailActivity.this).startBtn();
                                                 finish();
                                             } else {
                                                 oopsLogout(response.body().getMeta().getMessage());
                                             }
                                         }
                                     } else {
                                         oops(Constants.SOME_THING_WRONG);
                                     }
                                 } else {
                                     oops(Constants.SOME_THING_WRONG);
                                 }
                             }

                             @Override
                             public void onFailure(Throwable t) {
                                 hideLoader(progress);
                             }
                         }

        );
    }
}
