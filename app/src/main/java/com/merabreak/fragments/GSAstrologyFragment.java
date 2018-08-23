package com.merabreak.fragments;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseFragment;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.ArticleActivity_;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.OfflineChallengesActivity_;
import com.merabreak.adapter.AstrologyRecyclerViewAdapter;
import com.merabreak.adapter.ChallengeRecyclerViewAdapter;
import com.merabreak.models.GaneshaSpeaks;
import com.merabreak.models.challenge.Astrology;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.gs_astrology)
public class GSAstrologyFragment extends BaseFragment {

    @FragmentArg
    GaneshaSpeaks ganeshaSpeaks;

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private AstrologyRecyclerViewAdapter recyclerListItemsAdapter;

    public List<Astrology> items = new ArrayList<Astrology>();

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    @AfterViews
    void init() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        manager.setStackFromEnd(false);
        GridLayoutManager manager1 = new GridLayoutManager(getActivity(), 2);
        manager1.setReverseLayout(false);
        manager1.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager1);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        items = ganeshaSpeaks.getAstrology();
    }

    @Override
    public void onResume() {
        super.onResume();
        makeViewsGone();
        if (!Connectivity.isConnected(getActivity())) {
            setProblem(context.getResources().getString(R.string.no_internet), "1");
        } else {
            drawItems();
        }
    }

    private void drawItems() {
        if (getActivity() != null) {

            if (items.size() > 0)
                recyclerListItems.setVisibility(View.VISIBLE);

            recyclerListItemsAdapter = new AstrologyRecyclerViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            ((AstrologyRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new AstrologyRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(Astrology astrology, View v) {
                        ArticleActivity_.intent(context).astrology(astrology).start();
                }
            });
        }
        if (items.size() > 0)
            problem.setVisibility(View.GONE);
    }

    private void setProblem(String errorString, String tag) {
        problem.setVisibility(View.VISIBLE);
        error.setText(errorString);
        error.setTag(tag);
        if (tag.equalsIgnoreCase("1")) {
            retry.setText(R.string.settings_text);
        } else {
            retry.setText(R.string.retry_text);
        }
    }

    @Click(R.id.retry)
    void onRetryClick() {
        if (error.getTag() != null) {
            if (error.getTag().toString().equalsIgnoreCase("1")) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            } else if (error.getTag().toString().equalsIgnoreCase("2")) {

            } else if (error.getTag().toString().equalsIgnoreCase("3")) {

            }
        }
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }
}
