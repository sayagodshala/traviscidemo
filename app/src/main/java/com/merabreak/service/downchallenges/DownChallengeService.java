package com.merabreak.service.downchallenges;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.merabreak.AppSettings;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.models.APIResponse;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.StartChallenge;
import com.merabreak.models.User;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Memimages;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.event.IDownloadEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Vinay on 10/17/2016.
 */
public class DownChallengeService extends Service {

    private Context mContext;
    Activity mActivity;

    APIService apiService;
    private User user = null;
    StartChallenge startChallenge;

    private List<DownloadedChallenges> downloadedChallenges;

    private int counter = 0;

    List<String> downloadURLs = new ArrayList<>();
    private int totalCounts = 0;
    private int finalCounts = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("Down ChallengeService", "fired");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        mContext = this;
        mActivity = new Activity();
        try {
            user = Util.getUser(mContext);
            if(user!=null){
                downloadedChallenges = Util.getDownChalSlugs(mContext);
                if (downloadedChallenges.size() > 0) {
                    Constants.USER_AUTH_TOKEN = user.getAuthToken();
                    apiService = APIClient.getAPIService();
                    saveOfflineChallenge();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void saveOfflineChallenge(){
        if (downloadedChallenges.size() == 0)
            return;

        Call<APIResponse<Challenge>> callback = apiService.saveChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), downloadedChallenges.get(counter).getSlug());
        callback.enqueue(new retrofit.Callback<APIResponse<Challenge>>() {
            @Override
            public void onResponse(Response<APIResponse<Challenge>> response) {
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(mContext, user);
                                saveOfflineChallenge();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    runDownloadTask(response.body().getValues());
                                    DbHelper.getInstance(mContext).saveChallenge(response.body().getValues(), "offline");
                                    Util.deleteDownChallenge(mContext, downloadedChallenges.get(counter).getSlug());
                                    downloadedChallenges.remove(counter);
                                    Util.saveDownChalSlugs(mContext, downloadedChallenges);
                                    saveOfflineChallenge();
                                } else {
                                    Util.makeToast(mContext, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(mContext, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(mContext, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if(t.getMessage()!=null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(mContext, t.getMessage());
                }else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    public void oopsLogout(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(mContext.getResources().getString(R.string.label_ok), (dialog, which) -> {
            clearAllUserDataAndFinish();
        }).show();
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(mContext).clearTable();
        SharedPreferences settings = mContext.getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        mActivity.finish();
    }

    private void runDownloadTask(Challenge challenge) {
        downloadURLs = new ArrayList<>();
        for (int i = 0; i < challenge.getSteps().size(); i++) {
            Step s = challenge.getSteps().get(i);
            if (s.getUrl() != null && !s.getUrl().equalsIgnoreCase("") && s.getUrl().toLowerCase().contains("http://") && !s.getUrl().toLowerCase().contains("youtube.com")) {
                downloadURLs.add(s.getUrl());
               // Log.d("Downloadable URL", s.getUrl());
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
                            images.getUrl().toLowerCase().contains("http://") &&
                            !images.getUrl().toLowerCase().contains("youtube.com")) {
                        downloadURLs.add(images.getUrl());
                       // Log.d("im map Downloadable URL", images.getUrl());
                    }
                }
            }
        }
        //image mapping

        if (challenge.getBackgroundImage() != null && challenge.getBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getBackgroundImage());
           // Log.d("back Downloadable URL", challenge.getBackgroundImage());
        }

        if (challenge.getStartBackgroundImage() != null && challenge.getStartBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getStartBackgroundImage());
            //Log.d("start Downloadable URL", challenge.getStartBackgroundImage());
        }

        if (challenge.getEndBackgroundImage() != null && challenge.getEndBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getEndBackgroundImage());
            //Log.d("end Downloadable URL", challenge.getEndBackgroundImage());
        }

        if (challenge.getCoverImage() != null && challenge.getCoverImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getCoverImage());
           // Log.d("cover Downloadable URL", challenge.getCoverImage());
        }

        if (challenge.getDealImage() != null && challenge.getDealImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getDealImage());
            //Log.d("deal Downloadable URL", challenge.getDealImage());
        }

        if (downloadURLs.size() > 0) {
            multiParallelDownload(challenge.getSlug());
        } else {
           // Log.d("DownloadTask", "NoDownloads");
        }
    }

    public void multiParallelDownload(String challengeSlug) {
        // Util.makeToast(getActivity(), "Wait... Downloading Challenge Media");
        final FileDownloadListener parallelTarget = createListener();
        int i = 0;
        // showLoader();
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
               // Log.d("DownloadTask", finalCounts + " - " + soFarBytes + " - " + totalBytes);
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
               // Log.d("Downloaded Path", task.getPath());
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
            //Log.d("DownloadTask", "Finished");
            finalCounts = 0;
            totalCounts = 0;
            downloadURLs = new ArrayList<>();
        }
    }
}
