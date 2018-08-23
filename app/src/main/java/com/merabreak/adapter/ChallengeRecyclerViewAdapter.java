package com.merabreak.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.analytics.Tracker;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.merabreak.AppSettings;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BusProvider;
import com.merabreak.ChallengeDownloadedEvent;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.EditProfileActivity_;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.User;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Memimages;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.merabreak.network.Connectivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit.Call;
import retrofit.Response;

public class ChallengeRecyclerViewAdapter extends RecyclerView
        .Adapter<ChallengeRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    public boolean offline = false;
    public List<Challenge> mDataset;
    private static MyClickListener myClickListener;
    Activity mActivity;
    Context mContext;
    public APIService apiService = APIClient.getAPIService();
    public User user;
    List<String> downloadURLs = new ArrayList<>();
    private int totalCounts = 0;
    private int finalCounts = 0;
    private ImageLoader mImageLoader;
    public Tracker gaTracker;
    private int lastPosition = -1;
    private final static int FADE_DURATION = 1000;
    private List<DownloadedChallenges> downloadedChallenges = new ArrayList<DownloadedChallenges>();
    DownloadedChallenges downChallenges = new DownloadedChallenges();

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener, View.OnLongClickListener {
        TextView type, title, total_plays, challenge_label;
        ImageView image, offline, offline_1, overflow_menu;
        RelativeLayout selector, mainContent;
        ProgressBar progress;

        public DataObjectHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            title = (TextView) itemView.findViewById(R.id.title);
            challenge_label = (TextView) itemView.findViewById(R.id.challenge_label);
            total_plays = (TextView) itemView.findViewById(R.id.total_plays);
            image = (ImageView) itemView.findViewById(R.id.image);
            offline = (ImageView) itemView.findViewById(R.id.offline);
            offline_1 = (ImageView) itemView.findViewById(R.id.offline_1);
            overflow_menu = (ImageView) itemView.findViewById(R.id.overflow_menu);
            selector = (RelativeLayout) itemView.findViewById(R.id.selector);
            mainContent = (RelativeLayout) itemView.findViewById(R.id.main_adapter_content);
            progress = (ProgressBar) itemView.findViewById(R.id.download_progress);
            itemView.setOnClickListener(this);
            offline.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            overflow_menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                switch (v.getId()) {
                    case R.id.offline:
                        GoogleAnalyticEvents.eventSignup(mContext, gaTracker, "Challenge Downloaded", "User Downloaded the Challenge", user.getFullName() + ": " + user.getPhone() + ": " + mDataset.get(getPosition()).getTitle());
                       /* Map<String, Object> eventValue = new HashMap<String, Object>();
                        eventValue.put(AFInAppEventParameterName.PARAM_10, mDataset.get(getPosition()).getTitle());
                        ((ApplicationSingleton) mContext.getApplicationContext()).appsFlyerLib().trackEvent(mContext, "Challenge Downloaded", eventValue);*/
                        if (Connectivity.isConnected(mContext)) {
                            if (Util.isMobileDataForOfflineDownload(mContext)) {
                                if (!Connectivity.isConnectedWifi(mContext)) {
                                    Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_using_data));
                                }
                              //  saveChallengeOffline(mDataset.get(getPosition()));
                            } else {
                                if (Connectivity.isConnectedWifi(mContext)) {
                               //     saveChallengeOffline(mDataset.get(getPosition()));
                                } else {
                                    Util.makeToast(mContext, mContext.getResources().getString(R.string.no_internet));
                                    EditProfileActivity_.intent(mContext).start();
                                }
                            }
                        } else {
                            Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_no_internet));
                            //Util.makeToast(mContext, "Ohho!No Internet Connection.Once connected to Internet will automatically download this challenge for you!");
                            //downChallenges.setSlug(mDataset.get(getPosition()).getSlug());
                            // downloadedChallenges.add(downChallenges);
                            // Util.saveDownChalSlugs(mContext, downloadedChallenges);
                        }
                        break;
                    case R.id.overflow_menu:
                        showPopupMenu();
                        break;
                    default:
                        myClickListener.onItemClick(mDataset.get(getPosition()), v);
                        break;
                }
            }
        }

        private void showPopupMenu() {
            PopupMenu popup = new PopupMenu(mContext, overflow_menu);
            popup.inflate(R.menu.options_menu);
            MenuItem item = popup.getMenu().findItem(R.id.download);
            if(mDataset.get(getPosition()).getOffline() == 1){
                item.setVisible(true);
            }else{
                item.setVisible(false);
            }
            if(mDataset.get(getPosition()).getIsDownloaded() == 1){
                item.setTitle(mContext.getResources().getString(R.string.challenge_adapter_downloaded));
            }else{
                item.setTitle(mContext.getResources().getString(R.string.challenge_adapter_download));
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.download:
                            if(mDataset.get(getPosition()).getIsDownloaded() == 1){
                                Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_already_downloaded));
                            }else {
                                GoogleAnalyticEvents.eventSignup(mContext, gaTracker, "Challenge Downloaded", "User Downloaded the Challenge", user.getFullName() + ": " + user.getPhone() + ": " + mDataset.get(getPosition()).getTitle());
                                /*Map<String, Object> eventValue = new HashMap<String, Object>();
                                eventValue.put(AFInAppEventParameterName.PARAM_10, mDataset.get(getPosition()).getTitle());
                                ((ApplicationSingleton) mContext.getApplicationContext()).appsFlyerLib().trackEvent(mContext, "Challenge Downloaded", eventValue);*/
                                if (Connectivity.isConnected(mContext)) {
                                    if (Util.isMobileDataForOfflineDownload(mContext)) {
                                        if (!Connectivity.isConnectedWifi(mContext)) {
                                            Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_using_data));
                                        }
                                        saveChallengeOffline(mDataset.get(getPosition()));
                                    } else {
                                        if (Connectivity.isConnectedWifi(mContext)) {
                                            saveChallengeOffline(mDataset.get(getPosition()));
                                        } else {
                                            Util.makeToast(mContext, mContext.getResources().getString(R.string.no_internet));
                                            EditProfileActivity_.intent(mContext).start();
                                        }
                                    }
                                } else {
                                    Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_no_internet));
                                    //Util.makeToast(mContext, "Ohho!No Internet Connection.Once connected to Internet will automatically download this challenge for you!");
                                    //downChallenges.setSlug(mDataset.get(getPosition()).getSlug());
                                    // downloadedChallenges.add(downChallenges);
                                    // Util.saveDownChalSlugs(mContext, downloadedChallenges);
                                }
                            }
                            break;
                        case R.id.share:
                            shareChallenge();
                            break;
                    }
                    return false;
                }
            });
            popup.show();
        }

        private void shareChallenge(){
            GoogleAnalyticEvents.eventChallengeLike(mContext, gaTracker, "Challenge Shared", user.getFullName() + ": " + user.getPhone() + ": " + mDataset.get(getPosition()).getTitle());
          /*  Map<String, Object> eventValue = new HashMap<String, Object>();
            eventValue.put(AFInAppEventParameterName.PARAM_6, mDataset.get(getPosition()).getTitle());
            ((ApplicationSingleton) mContext.getApplicationContext()).appsFlyerLib().trackEvent(mContext, "Challenge Shared", eventValue);*/
            BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                    // .setCanonicalIdentifier("item/12345")
                    .setTitle("MeraBreak Challenge")
                    //  .setContentDescription("My Content Description")
                    //  .setContentImageUrl("https://example.com/mycontent-12345.png")
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .addContentMetadata("action", "open_challenge")
                    .addContentMetadata("slug", mDataset.get(getPosition()).getSlug());

            LinkProperties linkProperties = new LinkProperties()
                    .setChannel("facebook")
                    .setFeature("sharing");
            // .addControlParameter("$desktop_url", "http://example.com/home")
            // .addControlParameter("$ios_url", "http://example.com/ios");

            branchUniversalObject.generateShortUrl(mContext, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (error == null) {
                        //Log.i("MyApp", "got my Branch link to share: " + url);
                        Util.challengeShareApp(mActivity, "MeraBreak", "MeraBreak Challenge - Share Via:", url);
                    }
                }
            });
        }

        private void saveChallengeOffline(Challenge challenge) {
            if (challenge.getOffline() == 1) {
               // progress.setVisibility(View.VISIBLE);
               // offline.setVisibility(View.GONE);
                Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_downloading) + " " + challenge.getTitle() + " " + mContext.getResources().getString(R.string.challenge_adapter_challenge));
                Call<APIResponse<Challenge>> callback = apiService.saveChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), challenge.getSlug());
                callback.enqueue(new retrofit.Callback<APIResponse<Challenge>>() {
                    @Override
                    public void onResponse(Response<APIResponse<Challenge>> response) {
                        if(response.code() == 200 && response.body() != null) {
                            if (response.body().getMeta() != null) {
                                if (response.body().getMeta().getStatusCode() == 401) {
                                    if (response.body().getMeta().isStatus()) {
                                        user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                        Util.setUser(mContext, user);
                                        saveChallengeOffline(challenge);
                                    } else {
                                        oopsLogout(response.body().getMeta().getMessage());
                                    }
                                } else {
                                    if (response.body().getMeta().isStatus()) {
                                        if (response.body().getValues() != null) {
                                            runDownloadTask(response.body().getValues());
                                            for (int i = 0; i < mDataset.size(); i++) {
                                                if (challenge.getSlug().equalsIgnoreCase(mDataset.get(i).getSlug())) {
                                                    if (mDataset.size() > 0)
                                                        Util.saveHomeChallenge(mContext, mDataset);
                                                    else
                                                        Util.saveHomeChallenge(mContext, null);

                                                    break;
                                                }
                                            }
                                            DbHelper.getInstance(mContext).saveChallenge(response.body().getValues(), "offline");
                                        } else {
                                            Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                                        }
                                    } else {
                                        Util.makeToast(mContext, response.body().getMeta().getMessage());
                                        //  progress.setVisibility(View.GONE);
                                        //  offline.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                            }
                        }else{
                            Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                      //  progress.setVisibility(View.GONE);
                      //  offline.setVisibility(View.VISIBLE);
                        Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                    }
                });
            } else {
                Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_cannot_play_offline));
            }
        }

        public void oopsLogout(String message) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getResources().getString(R.string.label_oops));
            builder.setCancelable(false);
            builder.setMessage(message).setPositiveButton(mContext.getResources().getString(R.string.ok), (dialog, which) -> {
                clearAllUserDataAndFinish();
            }).show();
        }

        private void clearAllUserDataAndFinish() {
            DbHelper.getInstance(mContext).clearTable();
            SharedPreferences settings = mContext.getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            settings.edit().clear().apply();
            SplashActivity_.intent(mContext).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
            mActivity.finish();
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
                    //Log.d("video url", s.getUrl());
                } else if (s.getImage() != null && s.getImage().toLowerCase().contains("http://")) {
                    downloadURLs.add(s.getImage());
                    //Log.d("image 1", s.getImage());
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
                                images.getUrl().toLowerCase().contains("http://")) {
                            downloadURLs.add(images.getUrl());
                           // Log.d("memory game images", images.getUrl());
                        }
                    }
                }
            }
            //image mapping

            if (challenge.getBackgroundImage() != null && challenge.getBackgroundImage().toLowerCase().contains("http")) {
                downloadURLs.add(challenge.getBackgroundImage());
                //Log.d("background image", challenge.getBackgroundImage());
            }

            if (challenge.getStartBackgroundImage() != null && challenge.getStartBackgroundImage().toLowerCase().contains("http")) {
                downloadURLs.add(challenge.getStartBackgroundImage());
               // Log.d("start image", challenge.getStartBackgroundImage());
            }

            if (challenge.getEndBackgroundImage() != null && challenge.getEndBackgroundImage().toLowerCase().contains("http")) {
                downloadURLs.add(challenge.getEndBackgroundImage());
               // Log.d("end image", challenge.getEndBackgroundImage());
            }

            if (challenge.getCoverImage() != null && challenge.getCoverImage().toLowerCase().contains("http")) {
                downloadURLs.add(challenge.getCoverImage());
               // Log.d("cover image", challenge.getCoverImage());
            }

            if (challenge.getDealImage() != null && challenge.getDealImage().toLowerCase().contains("http")) {
                downloadURLs.add(challenge.getDealImage());
              //  Log.d("deal image", challenge.getDealImage());
            }

            if (downloadURLs.size() > 0) {
                multiParallelDownload(challenge.getSlug());
            } else {
                Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_download_completed));
               // progress.setVisibility(View.GONE);
              //  offline.setVisibility(View.GONE);
               // offline_1.setVisibility(View.VISIBLE);
                PopupMenu popup = new PopupMenu(mContext, overflow_menu);
                popup.inflate(R.menu.options_menu);
                MenuItem item = popup.getMenu().findItem(R.id.download);
                item.setTitle(mContext.getResources().getString(R.string.challenge_adapter_downloaded));
                mDataset.get(getPosition()).setIsDownloaded(1);
                BusProvider.bus().post(new ChallengeDownloadedEvent());
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
                    if (mActivity.isFinishing()) {
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
              //  progress.setVisibility(View.GONE);
              //  offline.setVisibility(View.GONE);
              //  offline_1.setVisibility(View.VISIBLE);
                PopupMenu popup = new PopupMenu(mContext, overflow_menu);
                popup.inflate(R.menu.options_menu);
                MenuItem item = popup.getMenu().findItem(R.id.download);
                item.setTitle(mContext.getResources().getString(R.string.challenge_adapter_downloaded));
                mDataset.get(getPosition()).setIsDownloaded(1);
                BusProvider.bus().post(new ChallengeDownloadedEvent());
                Util.makeToast(mContext, mContext.getResources().getString(R.string.challenge_adapter_download_completed));
                //Log.d("DownloadTask", "Finished");
                finalCounts = 0;
                totalCounts = 0;
                downloadURLs = new ArrayList<>();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (myClickListener != null) {
                myClickListener.onLongItemClick(mDataset.get(getPosition()), v);
                return true;
            }
            return false;
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ChallengeRecyclerViewAdapter(List<Challenge> myDataset, Activity activity, Context context) {
        mDataset = myDataset;
        mActivity = activity;
    }

    public ChallengeRecyclerViewAdapter(List<Challenge> myDataset, Context context, boolean offline) {
        mDataset = myDataset;
        this.offline = offline;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item_challange, parent, false);
        mContext = parent.getContext();
        FontUtils.setDefaultFont(mContext, view);
        user = Util.getUser(mContext);
        gaTracker = ((ApplicationSingleton) mContext.getApplicationContext()).googleAnalyticsTracker();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Challenge item = mDataset.get(position);
       /* if (!offline) {
            if (item.getOffline() == 1) {
                // holder.offline.setVisibility(View.VISIBLE);
            } else {
                //  holder.offline.setVisibility(View.GONE);
            }
        }
        if (item.getIsDownloaded() == 1) {
            holder.offline.setVisibility(View.GONE);
            holder.offline_1.setVisibility(View.VISIBLE);
        } else {
            holder.offline_1.setVisibility(View.GONE);
        }*/

        if (offline) {
            holder.total_plays.setVisibility(View.GONE);
            holder.overflow_menu.setVisibility(View.GONE);
        } else {
            holder.total_plays.setVisibility(View.VISIBLE);
            holder.overflow_menu.setVisibility(View.VISIBLE);
        }
        if(item.getChallengeType() != null && item.getChallengeType().equalsIgnoreCase("article"))
            holder.overflow_menu.setVisibility(View.GONE);

        if (item.getCategory().size() > 0)
            holder.type.setText(item.getCategory().get(0).getTitle());
        else
            holder.type.setVisibility(View.GONE);

        if (item.getCategory().size() > 0) {
            if (item.getCategory().get(0).getTitle() != null)
                holder.challenge_label.setText("   " + item.getCategory().get(0).getTitle() + "   ");

            if (item.getCategory().get(0).getColor() != null)
                holder.challenge_label.getBackground().setColorFilter(Color.parseColor(item.getCategory().get(0).getColor()), PorterDuff.Mode.SRC_IN);

        } else {
            holder.challenge_label.setVisibility(View.GONE);
        }
        holder.title.setText(item.getTitle());
        holder.total_plays.setText(item.getTotalPlayed());

        try {
            if (!item.getContentLanguage().equalsIgnoreCase("en"))
                holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        }catch (NullPointerException e)
        {
            Log.e(LOG_TAG, e.toString());
        }

        if (item.getCoverImage() != null && item.getCoverImage().contains("http")) {

            Picasso.with(mContext).load(item.getCoverImage()).fit().centerCrop().placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        } else {
            holder.selector.setVisibility(View.GONE);
        }

    }

    public void addItem(Challenge dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(Challenge position, View v);

        public void onLongItemClick(Challenge position, View v);

        public void onOfflineClick(Challenge position, View v);
    }
}