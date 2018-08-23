package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.PrizeListAdapter;
import com.merabreak.adapter.WinnersRecyclerViewAdapter;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.PrizeModel;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Memimages;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.Connectivity;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by sayagodshala on 25/01/16.
 */
@EActivity(R.layout.challenge_detail_new)
public class ChallengeDetailActivity extends BaseActivity {

    @Extra
    Challenge challenge;

    @Extra
    String tagBelongTo;

    @Extra
    String slug;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.image)
    ImageView image;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.category)
    TextView category;

    @ViewById(R.id.rules)
    TextView rules;

    @ViewById(R.id.start)
    Button startBtn;

    @ViewById(R.id.relative_main)
    RelativeLayout relative_main;

    @ViewById(R.id.chall_det_linear)
    LinearLayout challengeDetailsLayout;

    @ViewById(R.id.prize_hldr)
    LinearLayout prizeHldr;

    @ViewById(R.id.TnC_hldr)
    RelativeLayout TnCHldr;

    @ViewById(R.id.tc_title_txt)
    TextView tcTitleTxt;

    @ViewById(R.id.tc_desc_txt)
    TextView tcDescTxt;

    @ViewById(R.id.winner_items)
    RecyclerView horizontalRecyclerListItems;

    @ViewById(R.id.winners_label)
    TextView winnersLabel;

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.challenge_details_content)
    RelativeLayout challenge_details_content;

   /* @ViewById(R.id.challenge_details_content_scr)
    ScrollView challenge_details_content_scr;*/

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    TextView retry;

    @ViewById(R.id.offline)
    ImageView offlineIcon;

    @ViewById(R.id.download_progress)
    ProgressBar download_progress;

    @ViewById(R.id.chall_title)
    TextView challTitle;

    @ViewById(R.id.chall_about)
    TextView challAbout;

    @ViewById(R.id.chall_rule)
    TextView challRule;

    @ViewById(R.id.challenge_qn_num)
    TextView challQnNum;

    @ViewById(R.id.challenge_points_num)
    TextView challPointNum;

    @ViewById(R.id.linear_two_view)
    View pointLinearTwoView;

    @ViewById(R.id.challenge_time_num)
    TextView challTime;

    @ViewById(R.id.linear_two)
    LinearLayout pointLinearTwo;


    List<String> downloadURLs = new ArrayList<>();
    private int totalCounts = 0;
    private int finalCounts = 0;

    private RecyclerView.Adapter recyclerListItemsAdapter;
    AlertDialog alertDialog;

    private PrizeListAdapter prizeListAdapter;

    @ViewById(R.id.prize_rv)
    RecyclerView prizeRv;

    @ViewById(R.id.desc_txt)
    TextView prizeDescTxt;

    @Extra
    boolean offline;
    private List<PrizeModel> prizeList = new ArrayList<>();

    @AfterViews
    void init() {
        // toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Log.d("Challenge Detail", new Gson().toJson(challenge));
        imageLoader = new ImageLoader(this);
        startBtn.setVisibility(View.GONE);
        title.setText("");
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
       // toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tagBelongTo ==null || tagBelongTo.isEmpty() )
                    onBackPressed();
                else if(tagBelongTo.equals(RaffleContestActivity.class.getName()))
                {
                    finish();
                }

            }
        });

        String filePath = (challenge != null) ? challenge.getBackgroundImage() : "";
        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = challenge.getBackgroundImage().substring(challenge.getBackgroundImage().lastIndexOf('/') + 1, challenge.getBackgroundImage().length());
                String savedFile = Constants.FOLDER_PATH(challenge.getSlug(), fileName);
                File file = new File(savedFile);
                if (file.exists()) {
                    Uri imageURI = Uri.parse(savedFile);
                    //Log.d("Downloaded IMGURI", savedFile);
                    image.setImageURI(imageURI);
                } else {
                    //Util.makeToast(context, "Image Not found");
                }
            }
        } else {
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
        }

        if (challenge != null) {
            name.setText(challenge.getTitle());
            challTitle.setText(challenge.getTitle());

            if (challenge.getCategory() != null && challenge.getCategory().size() > 0)
                category.setText(challenge.getCategory().get(0).getTitle());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (offline) {
            setData();
            problem.setVisibility(View.GONE);
        } else {
            if (!Connectivity.isConnected(this)) {
                makeViewsGone();
                setProblem(getResources().getString(R.string.no_internet), "1");
            } else {
                if (startBtn.getVisibility() == View.GONE) {
                    makeViewsGone();
                    getChallengeDetail();
                }
            }
        }
    }

    private void getChallengeDetail() {
        hideLoader(progress);
        showLoader(progress);

        Call<APIResponse<Challenge>> callback = null;
        if (challenge != null)
            callback = apiService.getChallengeDetail(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug(), Util.getDeviceDPI(this));
        else
            callback = apiService.getChallengeDetail(Constants.MB_API_KEY, user.getAuthToken(), slug, Util.getDeviceDPI(this));

        callback.enqueue(new Callback<APIResponse<Challenge>>() {
            @Override
            public void onResponse(Response<APIResponse<Challenge>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengeDetailActivity.this, user);
                                getChallengeDetail();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        }
                        if (response.body().getMeta().getStatusCode() == 204) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengeDetailActivity.this, user);
                                getChallengeDetail();
                            } else {
                                oopsError(response.body().getMeta().getMessage());
                            }
                        }

                        else {
                            if (response.body().getMeta().isStatus()) {
                                //Log.d("Challenge Data", new Gson().toJson(response.body().getValues()));
                                if (response.body().getValues() != null) {
                                    challenge = response.body().getValues();
                                    setData();
                                } else {
                                    if (Connectivity.isConnected(ChallengeDetailActivity.this))
                                        setProblem(response.body().getMeta().getMessage(), "3");
                                    else
                                        setProblem(context.getResources().getString(R.string.no_internet), "3");
                                }
                            } else {
                                if (Connectivity.isConnected(ChallengeDetailActivity.this))
                                    setProblem(response.body().getMeta().getMessage(), "3");
                                else
                                    setProblem(context.getResources().getString(R.string.no_internet), "3");
                            }
                        }
                    } else {
                        if (Connectivity.isConnected(ChallengeDetailActivity.this))
                            setProblem(Constants.SOME_THING_WRONG, "2");
                        else
                            setProblem(context.getResources().getString(R.string.no_internet), "2");
                    }
                }else{
                    if (Connectivity.isConnected(ChallengeDetailActivity.this))
                        setProblem(Constants.SOME_THING_WRONG, "2");
                    else
                        setProblem(context.getResources().getString(R.string.no_internet), "2");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (Connectivity.isConnected(ChallengeDetailActivity.this))
                    setProblem(Constants.SOME_THING_WRONG, "2");
                else
                    setProblem(context.getResources().getString(R.string.no_internet), "2");
            }
        });
    }

    public void oopsError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(context.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {

            dialog.dismiss();
            finish();
        }).show();
    }

    private void setData() {
        challenge_details_content.setVisibility(View.VISIBLE);
        //challenge_details_content_scr.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.VISIBLE);
        description.setText(challenge.getDescription());
        rules.setText(challenge.getRules());
        challAbout.setText(challenge.getDescription());
        challRule.setText(challenge.getRules());
        challQnNum.setText(String.valueOf(challenge.getCountQuestion()));
        if (challenge.getBackgroundColor() != null)
            relative_main.setBackgroundColor(Color.parseColor(challenge.getBackgroundColor()));

        if(Integer.valueOf(challenge.getContest_challenge()) == 1)
        {
            pointLinearTwo.setVisibility(View.GONE);
            pointLinearTwoView.setVisibility(View.GONE);
        }
        else {
            pointLinearTwo.setVisibility(View.VISIBLE);
            pointLinearTwoView.setVisibility(View.VISIBLE);
        }

        challPointNum.setText(""+ challenge.getCoins() + "");

        int timeLimit = challenge.getTimeLimit();

        if (timeLimit > 0) {
            long totalMillis = timeLimit * 1000;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));
            // duration.setText(hms);
            challTime.setText(hms);
        } else {
            // duration.setText("0");
            challTime.setText("0");
        }

        /*if (challenge.getDealTitle() != null) {
            deals.setVisibility(View.VISIBLE);
        } else {
            deals.setVisibility(View.GONE);
        }*/

        if (challenge.getSteps().size() > 0) {
            startBtn.setText(R.string.play);
        } else {
            startBtn.setCompoundDrawables(null, null, null, null);
            startBtn.setTextSize(18);
            startBtn.setText(R.string.participate);
        }


        if (slug != null) {
            name.setText(challenge.getTitle());
            challTitle.setText(challenge.getTitle());

            if (challenge.getCategory() != null && challenge.getCategory().size() > 0)
                category.setText(challenge.getCategory().get(0).getTitle());

            String filePath = (challenge != null) ? challenge.getBackgroundImage() : "";
            if (filePath != null && filePath.contains("http")) {
                Picasso.with(context).load(filePath).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
        }

        if(Integer.valueOf(challenge.getContest_challenge()) != 1 && challenge.getChallengeRules() != null && !challenge.getChallengeRules().equalsIgnoreCase("")){
            rulesAlert();
        }

    }

    private void rulesAlert(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.challenge_rules_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView rulesTitle = (TextView) promptView.findViewById(R.id.rules_title);
        TextView rulesDesc = (TextView) promptView.findViewById(R.id.rules_desc);
        rulesDesc.setText(challenge.getChallengeRules());
        Button rulesClose = (Button) promptView.findViewById(R.id.rules_close);
        rulesTitle.setTypeface(font);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        rulesClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void drawWinners() {

        if (challenge.getWinners().size() > 0) {
            horizontalRecyclerListItems.setVisibility(View.VISIBLE);
            winnersLabel.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerListItems.setLayoutManager(manager);
        horizontalRecyclerListItems.setNestedScrollingEnabled(false);
        horizontalRecyclerListItems.setHasFixedSize(false);

        recyclerListItemsAdapter = new WinnersRecyclerViewAdapter(challenge.getWinners(), this);
        horizontalRecyclerListItems.setAdapter(recyclerListItemsAdapter);
    }


    @Click(R.id.start)
    void onStartChallenge() {

        if(Integer.valueOf(challenge.getContest_challenge()) == 1 &&
                startBtn.getText().equals(getResources().getString(R.string.play)) &&
                challenge.getPrize_list() !=null &&
                challenge.getPrize_list().size()>0
                ){

            setAdapter();
            prizeList.clear();
            prizeList.addAll(challenge.getPrize_list());

            showPrizesList();

        }
        else if(Integer.valueOf(challenge.getContest_challenge()) == 1 &&
                (startBtn.getText().equals(getResources().getString(R.string.next_in_caps)) || startBtn.getText().equals(getResources().getString(R.string.play)))
                && !challenge.getChallengeRules().isEmpty()){

            showTnC();

        }else {

            if (challenge.getSteps().size() > 0) {
                if (offline)
                    ChallengePlayActivity_.intent(ChallengeDetailActivity.this).challange(challenge).offline(true).flags(WindowManager.LayoutParams.FLAG_FULLSCREEN).start();
                else {
                    if (Connectivity.isConnected(this)) {
                        GoogleAnalyticEvents.event(this, gaTracker, "Challenge Click", "User Playing Challenge", user.getFullName() + ": " + user.getPhone() + ": " + challenge.getTitle());

                        startChallenge();
                    } else
                        justAlert(getResources().getString(R.string.no_internet));
                }
            } else {
                startChallenge();
            }
        }
    }

    private void setAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        prizeRv.setLayoutManager(manager);
        prizeRv.setNestedScrollingEnabled(false);
        prizeRv.setHasFixedSize(false);

        prizeList = new ArrayList<>();

        prizeListAdapter = new PrizeListAdapter(this,prizeList);
        prizeRv.setAdapter(prizeListAdapter);

    }

    private void showPrizesList()
    {
        challengeDetailsLayout.setVisibility(View.GONE);
        prizeHldr.setVisibility(View.VISIBLE);

        if(!Util.textIsEmpty(challenge.getPrize_subtitle()))
        {
            prizeDescTxt.setText(Util.getHtmlFormatString(challenge.getPrize_subtitle()));
        }

        prizeDescTxt.setVisibility(View.VISIBLE);
        startBtn.setText(getResources().getString(R.string.next_in_caps));
        prizeListAdapter.notifyDataSetChanged();
    }

    private void showTnC()
    {
        challengeDetailsLayout.setVisibility(View.GONE);
        prizeHldr.setVisibility(View.GONE);
        TnCHldr.setVisibility(View.VISIBLE);
        startBtn.setText(getResources().getString(R.string.i_agree));
        tcDescTxt.setText(challenge.getChallengeRules());
    }

    private void startChallenge() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<StartChallenge>> callback = apiService.setChallengeStart(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug());
        callback.enqueue(new Callback<APIResponse<StartChallenge>>() {
                             @Override
                             public void onResponse(Response<APIResponse<StartChallenge>> response) {
                                 hideLoader(progress);
                                 if(response.code() == 200 && response.body() != null) {
                                     if (response.body().getMeta() != null) {
                                         if (response.body().getMeta().getStatusCode() == 200) {
                                             if (response.body().getMeta().isStatus()) {
                                                 //Log.d("strat challenge 200", new Gson().toJson(response.body().getValues()));
                                                 Util.saveStartChallengeDetails(ChallengeDetailActivity.this, response.body().getValues());
                                                 ((ApplicationSingleton) getApplication()).isChallengePlaying = true;

                                                 ChallengePlayActivity_.intent(ChallengeDetailActivity.this).challange(challenge).offline(false).flags(WindowManager.LayoutParams.FLAG_FULLSCREEN).start();
                                                 finish();
                                             } else {
                                                 oops(response.body().getMeta().getMessage());
                                             }
                                         } else if (response.body().getMeta().getStatusCode() == 401) {
                                             if (response.body().getMeta().isStatus()) {
                                                 user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                                 Util.setUser(ChallengeDetailActivity.this, user);
                                                 startChallenge();
                                             } else {
                                                 oopsLogout(response.body().getMeta().getMessage());
                                             }
                                         } else {
                                             if (response.body().getMeta().getStatusCode() == 205) {
                                                 Util.makeToast(ChallengeDetailActivity.this, response.body().getMeta().getMessage());
                                                 //Log.d("startBtn challenge 205", new Gson().toJson(response.body().getValues()));
                                                 Util.saveStartChallengeDetails(ChallengeDetailActivity.this, response.body().getValues());
                                                 // MainActivity_.intent(ChallengeDetailActivity.this).startBtn();
                                                 finish();
                                             } else {
                                                 oopsLogout(response.body().getMeta().getMessage());
                                             }
                                         }
                                     } else {
                                         oops(Constants.SOME_THING_WRONG);
                                     }
                                 }else{
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

   /* @Click(R.id.image)
    void onImageClick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (challenge.getBackgroundImage().contains("http") || challenge.getBackgroundImage().contains("https")) {
            intent.setDataAndType(
                    Uri.parse(challenge.getBackgroundImage()),
                    "image*//*");
        }
        startActivity(intent);
    }*/

    @Click(R.id.deals)
    void onCheckDeals() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialoglayout = layoutInflater.inflate(R.layout.deal_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView dealTitle = (TextView) dialoglayout.findViewById(R.id.deal_name);
        TextView aboutDeal = (TextView) dialoglayout.findViewById(R.id.about_deal);
        TextView dealTC = (TextView) dialoglayout.findViewById(R.id.deal_tc);
        ImageView dealImage = (ImageView) dialoglayout.findViewById(R.id.deal_image);

        dealTitle.setText(challenge.getDealTitle());
        aboutDeal.setText(challenge.getDealAbout());
        dealTC.setText(challenge.getDealTC());
        String filePath = challenge.getDealImage();

        if (offline) {
            if (filePath != null && filePath.contains("http")) {
                String fileName = challenge.getDealImage().substring(challenge.getDealImage().lastIndexOf('/') + 1, challenge.getDealImage().length());
                String savedFile = Constants.FOLDER_PATH(challenge.getSlug(), fileName);
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
                Picasso.with(context).load(challenge.getDealImage()).into(dealImage, new com.squareup.picasso.Callback.EmptyCallback() {
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

    private void setProblem(String errorString, String tag) {
        problem.setVisibility(View.VISIBLE);
        error.setText(errorString);
        error.setTag(tag);
        if (tag.equalsIgnoreCase("1")) {
            retry.setVisibility(View.GONE);
        } else {
            retry.setText(R.string.retry_text);
        }
    }

    @Click(R.id.retry)
    void onRetryClick() {
        //Log.d("Retry", "clicked");
        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("1")) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            } else if (error.getTag().toString().equalsIgnoreCase("2")) {
                makeViewsGone();
                getChallengeDetail();
            } else if (error.getTag().toString().equalsIgnoreCase("3")) {
                makeViewsGone();
                getChallengeDetail();
            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        challenge_details_content.setVisibility(View.GONE);
        //challenge_details_content_scr.setVisibility(View.GONE);
        horizontalRecyclerListItems.setVisibility(View.GONE);
    }

    @Click(R.id.offline)
    void downloadChallenge() {
        if (Util.isMobileDataForOfflineDownload(context)) {
            Util.makeToast(context, getResources().getString(R.string.download_alert));
            saveChallengeOffline();
        } else {
            if (Connectivity.isConnectedWifi(context)) {
                saveChallengeOffline();
            } else {
                Util.makeToast(context, getResources().getString(R.string.data_enable));
                SettingsActivity_.intent(context).start();
            }
        }
    }

    private void saveChallengeOffline() {
        if (challenge.getOffline() == 1) {
            download_progress.setVisibility(View.VISIBLE);
            offlineIcon.setVisibility(View.GONE);
            Call<APIResponse<Challenge>> callback = apiService.saveChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug());
            callback.enqueue(new retrofit.Callback<APIResponse<Challenge>>() {
                @Override
                public void onResponse(Response<APIResponse<Challenge>> response) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ChallengeDetailActivity.this, user);
                                saveChallengeOffline();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    runDownloadTask(response.body().getValues());
                                    DbHelper.getInstance(ChallengeDetailActivity.this).saveChallenge(response.body().getValues(), "offline");
                                } else {
                                    Util.makeToast(ChallengeDetailActivity.this, Constants.SOME_THING_WRONG);
                                }
                            } else {
                                Util.makeToast(ChallengeDetailActivity.this, response.body().getMeta().getMessage());
                                download_progress.setVisibility(View.GONE);
                                offlineIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Util.makeToast(ChallengeDetailActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    download_progress.setVisibility(View.GONE);
                    offlineIcon.setVisibility(View.VISIBLE);
                    Util.makeToast(ChallengeDetailActivity.this, Constants.SOME_THING_WRONG);
                }
            });
        } else {
            Util.makeToast(ChallengeDetailActivity.this, getResources().getString(R.string.no_offline));
        }
    }

    private void runDownloadTask(Challenge challenge) {
        downloadURLs = new ArrayList<>();
        for (int i = 0; i < challenge.getSteps().size(); i++) {
            Step s = challenge.getSteps().get(i);
            if (s.getUrl() != null &&
                    !s.getUrl().equalsIgnoreCase("") &&
                    s.getUrl().toLowerCase().contains("http://") &&
                    !s.getUrl().toLowerCase().contains("youtube.com")) {
                downloadURLs.add(s.getUrl());
                //Log.d("Downloadable URL", s.getUrl());
            } else if (s.getImage() != null && s.getImage().toLowerCase().contains("http://")) {
                downloadURLs.add(s.getImage());
            }
        }

        //image mapping
        for (int i = 0; i < challenge.getSteps().size(); i++) {
            Step s = challenge.getSteps().get(i);
            if (s.getType().contentEquals("Image Mapping")) {
                for (int j = 0; j < s.getImages().size(); j++) {
                    Memimages images = s.getImages().get(j);
                    if (images.getUrl() != null &&
                            !images.getUrl().equalsIgnoreCase("") &&
                            images.getUrl().toLowerCase().contains("https://") &&
                            !images.getUrl().toLowerCase().contains("youtube.com")) {
                        downloadURLs.add(images.getUrl());
                        //Log.d("im map Downloadable URL", images.getUrl());
                    }
                }
            }
        }
        //image mapping

        if (challenge.getBackgroundImage() != null && challenge.getBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getBackgroundImage());
            //Log.d("back Downloadable URL", challenge.getBackgroundImage());
        }

        if (challenge.getStartBackgroundImage() != null && challenge.getStartBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getStartBackgroundImage());
            //Log.d("startBtn Downloadable URL", challenge.getStartBackgroundImage());
        }

        if (challenge.getEndBackgroundImage() != null && challenge.getEndBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getEndBackgroundImage());
            //Log.d("end Downloadable URL", challenge.getEndBackgroundImage());
        }

        if (challenge.getCoverImage() != null && challenge.getCoverImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getCoverImage());
            //Log.d("cover Downloadable URL", challenge.getCoverImage());
        }

        if (challenge.getDealImage() != null && challenge.getDealImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getDealImage());
            //Log.d("deal Downloadable URL", challenge.getDealImage());
        }

        if (downloadURLs.size() > 0) {
            multiParallelDownload(challenge.getSlug());
        } else {
            Util.makeToast(context, getResources().getString(R.string.download_done));
            //Log.d("Download", "Challenge Downloaded");
            download_progress.setVisibility(View.GONE);
            offlineIcon.setVisibility(View.VISIBLE);
            offlineIcon.setImageResource(R.drawable.ic_check_pressed);
            offlineIcon.setClickable(false);
            //Log.d("DownloadTask", "NoDownloads");
        }
    }

    public void multiParallelDownload(String challengeSlug) {
        final FileDownloadListener parallelTarget = createListener();
        int i = 0;
        for (String url : downloadURLs) {
            totalCounts++;
            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
            FileDownloader.getImpl().create(url)
                    .setListener(parallelTarget)
                    .setPath(Constants.FOLDER_PATH(challengeSlug, fileName))
                    .setCallbackProgressTimes(1)
                    .setTag(++i)
                    .ready();
        }

        FileDownloader.getImpl().start(parallelTarget, false);
    }

    private FileDownloadListener createListener() {
        return new FileDownloadListener() {

            @Override
            public boolean callback(IDownloadEvent event) {
                if (isFinishing()) {
                    return false;
                }
                return super.callback(event);
            }

            @Override
            protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {

            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);

            }

            @Override
            protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                //Log.d("DownloadTask", finalCounts + " - " + soFarBytes + " - " + totalBytes);
            }

            @Override
            protected void blockComplete(final BaseDownloadTask task) {
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                //Log.d("Downloaded Path", task.getPath());
                resetCounters();
            }

            @Override
            protected void paused(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                resetCounters();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                resetCounters();
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                resetCounters();
            }
        };
    }

    private void resetCounters() {
        finalCounts++;
        if (finalCounts == totalCounts) {
            download_progress.setVisibility(View.GONE);
            offlineIcon.setVisibility(View.VISIBLE);
            offlineIcon.setImageResource(R.drawable.ic_check_pressed);
            offlineIcon.setClickable(false);
            Util.makeToast(context, getResources().getString(R.string.download_done));
            //Log.d("DownloadTask", "Finished");
            finalCounts = 0;
            totalCounts = 0;
            downloadURLs = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tagBelongTo ==null || tagBelongTo.isEmpty() )
            MainActivity_.intent(this).start();


    }
}
