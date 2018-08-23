package com.merabreak.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.merabreak.AmountCoinsEvent;
import com.merabreak.BaseFragment;
import com.merabreak.BusProvider;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.RechargeActivity_;
import com.merabreak.activities.RechargeDetailsActivity;
import com.merabreak.adapter.MobileRechargeViewAdapter;
import com.merabreak.models.HomeRechargePlans;
import com.merabreak.models.RechargePlans;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Vinay on 7/26/2016.
 */
@EFragment(R.layout.recharge_plans)
public class RechargePlansFragment extends BaseFragment {

    @ViewById(R.id.items)
    RecyclerView recyclerListItems;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter recyclerListItemsAdapter;

    public List<RechargePlans> items = null;

    @ViewById(R.id.problem)
    LinearLayout problem;

    @ViewById(R.id.error)
    TextView error;

    @ViewById(R.id.retry)
    Button retry;

    @FragmentArg
    HomeRechargePlans homeRechargePlans;
    private String selectedTab;

    @AfterViews
    void init() {
        makeViewsGone();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        manager.setReverseLayout(false);
        manager.setStackFromEnd(false);
        recyclerListItems.setLayoutManager(manager);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);
        items = homeRechargePlans.getPlans();
        //Log.d("total plans", new Gson().toJson(homeRechargePlans));
        drawItems();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void makeViewsGone() {
        problem.setVisibility(View.GONE);
        recyclerListItems.setVisibility(View.GONE);
    }

    private void drawItems() {
        if (getActivity() != null) {
            problem.setVisibility(View.GONE);
            recyclerListItemsAdapter = new MobileRechargeViewAdapter(items, getActivity());
            recyclerListItems.setAdapter(recyclerListItemsAdapter);
            recyclerListItems.setVisibility(View.VISIBLE);
            ((MobileRechargeViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new MobileRechargeViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(RechargePlans position, View v) {
                    if(Integer.parseInt(position.getAmount()) >= 10) {
                        BusProvider.bus().post(new AmountCoinsEvent(
                                position.getAmount(),
                                position.getCoins(), position.getTabName()));
                         RechargeActivity_.intent(context).start();
                    }else{
                        Util.makeToast(context, context.getResources().getString(R.string.recharge_plans_frag_recharge_limit));
                    }
                }
            });
        }
    }

    public void setTabName(String tabName){
        selectedTab = tabName;
    }

}
