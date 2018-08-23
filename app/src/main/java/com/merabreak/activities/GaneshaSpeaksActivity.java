package com.merabreak.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.controls.FragmentAdapter;
import com.merabreak.fragments.GSAstrologyFragment;
import com.merabreak.fragments.GSAstrologyFragment_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.GaneshaSpeaks;
import com.merabreak.network.Connectivity;
import com.merabreak.network.ConnectivityReceiver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EActivity(R.layout.ganesha_speaks_activity)
public class GaneshaSpeaksActivity extends BaseActivity{

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.tabs)
    TabLayout tabs;

    @ViewById(R.id.viewpager)
    ViewPager viewpager;

    @ViewById(R.id.gs_no_internet)
    LinearLayout home_no_internet;

    List<Fragment> tabsFragments = new ArrayList<>();
    List<GaneshaSpeaks> ganeshaSpeaks = new ArrayList<GaneshaSpeaks>();

    @AfterViews
    void init(){
        GoogleAnalyticEvents.event(this, gaTracker, "GaneshaSpeaks.com - Horoscope", "User Clicked For GaneshaSpeaks Astrology", user.getFullName() + ": " + user.getPhone());
        title.setText(R.string.ganesha_speaks_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getTotalAstrologies();
    }

    private void setupViewPagerOld(){
        List<String> titles = new ArrayList<>();

        if (tabs.getChildCount() > 0)
            tabs.removeAllTabs();

        //titles.add(context.getResources().getString(R.string.gs_daily));
       // titles.add(context.getResources().getString(R.string.gs_weekly));
        //titles.add(context.getResources().getString(R.string.gs_monthly));
        tabs.setTabMode(TabLayout.MODE_FIXED);
        //tabsFragments.add(gsDailyFragment());
       // tabsFragments.add(gsWeeklyFragment());
       // tabsFragments.add(gsMonthlyFragment());
        if (viewpager.getAdapter() == null) {
            FragmentAdapter adapter =
                    new FragmentAdapter(getSupportFragmentManager(), tabsFragments);
            viewpager.setAdapter(adapter);
            tabs.setupWithViewPager(viewpager);
            tabs.setTabsFromPagerAdapter(adapter);
        }
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setText(titles.get(i));
        }
    }

   /* private GSDailyFragment gsDailyFragment() {
        return GSDailyFragment_.builder().build();
    }

    private GSWeeklyFragment gsWeeklyFragment() {
        return GSWeeklyFragment_.builder().build();
    }

    private GSMonthlyFragment gsMonthlyFragment() {
        return GSMonthlyFragment_.builder().build();
    }
*/

   private void getTotalAstrologies(){
       Call<APIResponse<List<GaneshaSpeaks>>> callback = apiService.getGSAstrologyList(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this));
       callback.enqueue(new Callback<APIResponse<List<GaneshaSpeaks>>>() {
           @Override
           public void onResponse(Response<APIResponse<List<GaneshaSpeaks>>> response) {
               if(response.code() == 200 && response.body() != null) {
                   if (response.body().getMeta() != null) {
                       if (response.body().getMeta().getStatusCode() == 401) {
                           if (response.body().getMeta().isStatus()) {
                               user.setAuthToken(response.body().getMeta().getNewAuthToken());
                               Util.setUser(GaneshaSpeaksActivity.this, user);
                               getTotalAstrologies();
                           } else {
                               oopsLogout(response.body().getMeta().getMessage());
                           }
                       } else {
                           if (response.body().getValues() != null) {
                               ganeshaSpeaks = response.body().getValues();
                                setupViewPager();
                           } else {
                               Util.makeToast(GaneshaSpeaksActivity.this, response.body().getMeta().getMessage());
                           }
                       }
                   } else {
                       Util.makeToast(GaneshaSpeaksActivity.this, Constants.SOME_THING_WRONG);
                   }
               } else {
                   Util.makeToast(GaneshaSpeaksActivity.this, Constants.SOME_THING_WRONG);
               }
           }

           @Override
           public void onFailure(Throwable t) {
           }
       });
   }

    private void setupViewPager() {
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < ganeshaSpeaks.size(); i++) {
            titles.add(ganeshaSpeaks.get(i).getName());
            tabs.addTab(tabs.newTab());
            if (ganeshaSpeaks.size() <= 3) {
                tabs.setTabMode(TabLayout.MODE_FIXED);
                tabs.setTabGravity(TabLayout.GRAVITY_FILL);
            } else {
                tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
            fragments.add(gsAstrologyFragment(ganeshaSpeaks.get(i)));
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

        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setText(titles.get(i));
        }

    }

    private GSAstrologyFragment gsAstrologyFragment(GaneshaSpeaks ganeshaSpeaks) {
        return GSAstrologyFragment_.builder().ganeshaSpeaks(ganeshaSpeaks).build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity_.intent(this).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    private void checkConnectivity() {
        if (!ConnectivityReceiver.isConnected(this)) {
            tabs.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
            home_no_internet.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.gs_tap)
    void tryAgain(){
        if (Connectivity.isConnected(this)) {
            finish();
            startActivity(getIntent());
        } else
            Util.makeToast(context, context.getResources().getString(R.string.main_activity_still_no_internet));
    }
}
