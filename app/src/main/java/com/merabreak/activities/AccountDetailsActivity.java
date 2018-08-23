package com.merabreak.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.controls.FragmentAdapter;
import com.merabreak.fragments.UserChallengesFragment;
import com.merabreak.fragments.UserChallengesFragment_;
import com.merabreak.fragments.UserCoinsFragment;
import com.merabreak.fragments.UserCoinsFragment_;
import com.merabreak.fragments.UserDealsFragment;
import com.merabreak.fragments.UserDealsFragment_;
import com.merabreak.fragments.UserProductsFragment;
import com.merabreak.fragments.UserProductsFragment_;
import com.merabreak.fragments.UserRafflesFragment;
import com.merabreak.fragments.UserRafflesFragment_;
import com.merabreak.models.AccountDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ewebcore on 14-Jan-16.
 */
@EActivity(R.layout.account_details)
public class AccountDetailsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.sub_title)
    TextView sub_title;

    @ViewById(R.id.tabs)
    TabLayout tabs;

    @ViewById(R.id.viewpager)
    ViewPager viewpager;

    @Extra
    int tabId;

    @AfterViews
    void init() {
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.account_details_screen_name);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        setupViewPager();
    }

    private void setupViewPager() {

       // int[] dIcons = {R.drawable.ic_ticks_white1, R.drawable.ic_challenges_red, R.drawable.ic_raffles_blue, R.drawable.ic_store_green}; //, R.drawable.ic_raffles_inactive

        AccountDetails ad = Util.getUserAccountDetails(this);

        List<String> titles = new ArrayList<>();
        titles.add("Points");
        titles.add("Challenges");
        titles.add("Raffle");
        titles.add("Store");

        List<String> sub_titles = new ArrayList<>();
        sub_titles.add(ad.getCoinBalance() + "");
        sub_titles.add(ad.getPlayedChallenge() + "");
        sub_titles.add(ad.getTotalRaffles() + "");
        sub_titles.add(ad.getTotalProducts() + "");
      //  titles.add(ad.getTotalDeals() + "");
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(userCoinsFragment());
        fragments.add(userChallengesFragment());
        fragments.add(userRafflesFragment());
        fragments.add(userProductsFragment());
       // fragments.add(userDealsFragment());
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

        viewpager.setCurrentItem(tabId, false);

        for (int i = 0; i < tabs.getTabCount(); i++) {
//            Drawable image = ContextCompat.getDrawable(this, dIcons[i]);
//            image.setBounds(0, 0, (int) (image.getIntrinsicWidth()), (int) (image.getIntrinsicHeight()));
//            SpannableString sb = new SpannableString(" ");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            View tabView = LayoutInflater.from(this).inflate(R.layout.list_item_account_details_tab, null);
            TextView t = (TextView) tabView.findViewById(R.id.title);
            TextView t1 = (TextView) tabView.findViewById(R.id.sub_title);
            ImageView img = (ImageView) tabView.findViewById(R.id.icon);
           // img.setImageDrawable(this.getResources().getDrawable(dIcons[i]));
            t.setText(titles.get(i));
            t1.setText(sub_titles.get(i));
            tabs.getTabAt(i).setCustomView(tabView);
        }

    }

    private UserChallengesFragment userChallengesFragment() {
        return UserChallengesFragment_.builder().build();
    }

    private UserCoinsFragment userCoinsFragment() {
        return UserCoinsFragment_.builder().build();
    }

    private UserProductsFragment userProductsFragment() {
        return UserProductsFragment_.builder().build();
    }

    private UserRafflesFragment userRafflesFragment() {
        return UserRafflesFragment_.builder().build();
    }

    private UserDealsFragment userDealsFragment() {
        return UserDealsFragment_.builder().build();
    }

}
