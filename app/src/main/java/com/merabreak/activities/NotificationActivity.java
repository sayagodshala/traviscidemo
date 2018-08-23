package com.merabreak.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.EndlessRecyclerViewScrollListener;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.NotificationRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.NotificationModel;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ewebcoremac1 on 02/07/18.
 */
@EActivity(R.layout.activity_notification)
public class NotificationActivity extends BaseActivity {
    private static String TAG = NotificationActivity.class.getName();

    @ViewById(R.id.notification_rv)
    RecyclerView notificationRv;

    @ViewById(R.id.placeholder)
    RelativeLayout placeHolder;

    @ViewById(R.id.cross_icon)
    ImageView crossImg;

    @ViewById(R.id.progress)
    LinearLayout progress;


    private ArrayList<NotificationModel> notificationList;

    private NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;


    @AfterViews
    void init()
    {
        setAdapter();

        callNotificationAPI();
    }

    @Click(R.id.cross_icon)
    void crossClick()
    {
        onBackPressed();
    }

    private void setAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRv.setLayoutManager(manager);
        notificationRv.setNestedScrollingEnabled(false);
        notificationRv.setHasFixedSize(false);

        notificationList = new ArrayList<>();

        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(this,notificationList);
        notificationRv.setAdapter(notificationRecyclerViewAdapter);


        notificationRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                callNotificationAPI();
            }
        });

        inItListener();

    }

    private void callNotificationAPI() {

        if (Connectivity.isConnectedWifi(context)) {
            progress.setVisibility(View.VISIBLE);
            notificationRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);

            Call<APIResponse<List<NotificationModel>>> callback = apiService.getNotificationList(Constants.MB_API_KEY, user.getAuthToken(), notificationList.size());
            callback.enqueue(new Callback<APIResponse<List<NotificationModel>>>() {
                @Override
                public void onResponse(Response<APIResponse<List<NotificationModel>>> response) {
                    progress.setVisibility(View.GONE);

                    //hideLoader(progress);
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(NotificationActivity.this, user);
                                    callNotificationAPI();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null &&  response.body().getValues().size() > 0) {

                                        notificationRv.setVisibility(View.VISIBLE);
                                        placeHolder.setVisibility(View.GONE);
                                        notificationList.addAll(response.body().getValues());

                                        notificationRecyclerViewAdapter.notifyDataSetChanged();
                                    }
                                    else if (response.body().getValues() != null && notificationList.size() == 0) {
                                        placeHolder.setVisibility(View.VISIBLE);
                                        notificationRv.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } else {
                            Util.makeToast(NotificationActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(NotificationActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });
        }else {
            progress.setVisibility(View.GONE);
            notificationRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }

    }

    private void inItListener() {

        ((NotificationRecyclerViewAdapter) notificationRecyclerViewAdapter).setOnItemClickListener(new NotificationRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(NotificationModel position, View v) {
               // ChallengeDetailActivity_.intent(context).tagBelongTo(RaffleContestActivity.class.getName()).challenge(position).startBtn();

                VoucherListsActivity_.intent(NotificationActivity.this).notification(position).start();
                finish();
            }
        });
    }
}
