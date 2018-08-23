package com.merabreak.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.merabreak.AmountCoinsEvent;
import com.merabreak.BaseActivity;
import com.merabreak.BusProvider;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.controls.FragmentAdapter;
import com.merabreak.fragments.RechargePlansFragment;
import com.merabreak.fragments.RechargePlansFragment_;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.HomeRechargePlans;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vinay on 7/26/2016.
 */
@EActivity(R.layout.recharge_details)
public class RechargeDetailsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.tabs)
    TabLayout tabs;

    @ViewById(R.id.viewpager)
    ViewPager viewpager;

    @Extra
    int tabId;

    List<HomeRechargePlans> homeRechargePlans = new ArrayList<HomeRechargePlans>();
    private String selectedTab;

    @AfterViews
    void init() {
        title.setText(R.string.recharge_details_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        setupViewPager();
    }

    private void setupViewPager() {
        int[] dIcons = {R.drawable.tab_challenge, R.drawable.tab_raffle, R.drawable.tab_store};
        homeRechargePlans = Util.getPlans(this);
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < homeRechargePlans.size(); i++) {
            titles.add(homeRechargePlans.get(i).getName());
            tabs.addTab(tabs.newTab());
            if (homeRechargePlans.size() <= 3) {
                tabs.setTabMode(TabLayout.MODE_FIXED);
                tabs.setTabGravity(TabLayout.GRAVITY_FILL);
            } else {
                tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
            fragments.add(rechargePlansFragment(homeRechargePlans.get(i)));
        }

        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
        tabs.setTabsFromPagerAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int[] icons = {R.drawable.ic_challenges_inactive, R.drawable.ic_raffles_inactive, R.drawable.ic_store_inactive};


        for (int i = 0; i < tabs.getTabCount(); i++) {
          /*  if(i == 0)
                selectedTab = titles.get(i);*/
            tabs.getTabAt(i).setText(titles.get(i));
        }



       /* tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getText().toString();
                Log.d("selected tab name", selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    private RechargePlansFragment rechargePlansFragment(HomeRechargePlans hrp) {
        return RechargePlansFragment_.builder().homeRechargePlans(hrp).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        AccountDetails ad = Util.getUserAccountDetails(getApplicationContext());
        if (ad != null)
            pointsNum.setText(doubleToStringNoDecimal(ad.getCoinBalance()));
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }

}
