package com.merabreak.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MainActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.RaffleContestAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


@EActivity(R.layout.activity_contest)
public class RaffleContestActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.redeem_voucher_rv)
    RecyclerView raffleContestRv;

    RaffleContestAdapter raffleContestAdapter;

    List<Challenge> contestChallengeList;
    @ViewById(R.id.problem)
    LinearLayout placeHolder;

    @ViewById(R.id.retry)
    TextView retry;


    @AfterViews
    void init() {
        //title.setText(R.string.raffle_challenge);
        title.setText(R.string.contests);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        contestChallengeList = new ArrayList<>();

        if (Connectivity.isConnectedWifi(context)) {
            raffleContestRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
            callContestChallengeList();
        }else {
            raffleContestRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        callContestChallengeList();
    }

    @Click(R.id.retry)
    void retryClick()
    {
        callContestChallengeList();
    }

    private void inItListener() {

        ((RaffleContestAdapter) raffleContestAdapter).setOnItemClickListener(new RaffleContestAdapter.MyClickListener() {
            @Override
            public void onItemClick(Challenge position, View v) {
                ChallengeDetailActivity_.intent(context).tagBelongTo(RaffleContestActivity.class.getName()).challenge(position).start();
            }
        });
    }


    private void callContestChallengeList() {
        /*hideLoader(progress);
        showLoader(progress);*/


        if (Connectivity.isConnectedWifi(context)) {
            raffleContestRv.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);



            String latLng = Util.getLatLng(RaffleContestActivity.this);
            JSONObject data = null;
            String[] coords = null;
            String cityId = null;
            try {
                data = new JSONObject().put("latLng",latLng);
                coords = data.getString("latLng").split(",");
                cityId = Util.getSelectedCityId(RaffleContestActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<APIResponse<List<Challenge>>> callback;

            if(coords!=null && coords.length ==2 && cityId != null && !cityId.isEmpty())
            {
                callback = apiService.getContestChallengeList(Constants.MB_API_KEY, user.getAuthToken(),  coords[0], coords[1],cityId);
            }
            else if((coords == null || coords.length !=2) && (cityId != null && !cityId.isEmpty()))
            {
                callback = apiService.getContestChallengeList(Constants.MB_API_KEY, user.getAuthToken(), "", "",cityId);
            }
            else if((coords != null && coords.length ==2) && (cityId == null || cityId.isEmpty()))
            {
                callback = apiService.getContestChallengeList(Constants.MB_API_KEY, user.getAuthToken(), coords[0], coords[1],"");
            }
            else
            {
                callback = apiService.getContestChallengeList(Constants.MB_API_KEY, user.getAuthToken());
            }
            callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                //hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RaffleContestActivity.this, user);
                                callContestChallengeList();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    contestChallengeList.clear();
                                    contestChallengeList.addAll(response.body().getValues());

                                    setAdapter();
                                } else {
                                    Util.makeToast(RaffleContestActivity.this, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(RaffleContestActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(RaffleContestActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RaffleContestActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
            });
        }else {
            raffleContestRv.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }

    }


    private void setAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        raffleContestRv.setLayoutManager(manager);
        raffleContestRv.setNestedScrollingEnabled(false);
        raffleContestRv.setHasFixedSize(false);

        raffleContestAdapter = new RaffleContestAdapter(contestChallengeList, this);
        raffleContestRv.setAdapter(raffleContestAdapter);

        inItListener();
    }



}
