package com.merabreak.activities;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.ChallengeRecyclerViewAdapter;
import com.merabreak.controls.DividerItemDecoration;
import com.merabreak.db.DbHelper;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EActivity(R.layout.offline_challenges)
public class OfflineChallengesActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.internet_label)
    TextView InternetWarning;

    @ViewById(R.id.empty)
    LinearLayout empty;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private RecyclerView.Adapter recyclerListItemsAdapter;

    public List<Challenge> items = new ArrayList<Challenge>();

    @AfterViews
    void init() {
        empty.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                onBackPressed();
            }
        });
        title.setText(R.string.offline_chall_title);

        items = DbHelper.getInstance(this).getAllChallenges();
        if (items.size() > 0) {
            drawChallenges();
        } else {
            empty.setVisibility(View.VISIBLE);
            if (!Connectivity.isConnected(this)) {
                InternetWarning.setVisibility(View.VISIBLE);
                InternetWarning.setText(R.string.offline_chall_no_internet_retry);
            }
        }

    }

    @Click(R.id.internet_label)
    void noInternet() {
        if (Connectivity.isConnected(this)) {
            SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        } else {
            Util.makeToast(OfflineChallengesActivity.this,  getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void drawChallenges() {
        recyclerListItems.setVisibility(View.VISIBLE);
        if (!Connectivity.isConnected(this)) {
            InternetWarning.setVisibility(View.VISIBLE);
            InternetWarning.setText(R.string.offline_chall_no_internet_retry);
        }
        // LinearLayoutManager manager = new LinearLayoutManager(this);
        //  manager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager manager1 = new GridLayoutManager(this, 2);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        recyclerListItemsAdapter = new ChallengeRecyclerViewAdapter(items, this, true);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        ((ChallengeRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ChallengeRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(Challenge position, View v) {
                //Log.i("Item Click", " Clicked on Item " + position);
                ChallengeDetailActivity_.intent(OfflineChallengesActivity.this).challenge(position).offline(true).start();
            }

            @Override
            public void onLongItemClick(Challenge position, View v) {

            }

            @Override
            public void onOfflineClick(Challenge position, View v) {

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        super.onNetworkConnectionChanged(isConnected);
        if (isConnected) {
            SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        } else {
            items = DbHelper.getInstance(this).getAllChallenges();
            if (items.size() > 0) {
                drawChallenges();
            } else {
                empty.setVisibility(View.VISIBLE);
                if (!Connectivity.isConnected(this)) {
                    InternetWarning.setVisibility(View.VISIBLE);
                    InternetWarning.setText(R.string.offline_chall_no_internet_retry);
                }
            }
        }
    }
}
