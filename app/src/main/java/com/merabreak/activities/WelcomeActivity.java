package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.MainActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ETPL-002 on 12-Jul-17.
 */
@EActivity(R.layout.welcome_layout)
public class WelcomeActivity extends BaseActivity {

    @ViewById(R.id.view_pager)
    ViewPager viewPager;

    @ViewById(R.id.btn_next)
    Button buttonNext;

    @ViewById(R.id.btn_skip)
    TextView buttonSkip;

    @ViewById(R.id.image)
    ImageView image;

    @ViewById(R.id.slide_one_text1)
    TextView slide_one_text1;

    @ViewById(R.id.slide_one_text2)
    TextView slide_one_text2;

    @ViewById(R.id.layoutDots)
    LinearLayout layoutDots;

    private TextView[] dots;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;

    @AfterViews
    void init() {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        layouts = new int[]{
                R.layout.slide_one,
                R.layout.slide_two,
                R.layout.slide_three,
                R.layout.slide_four,
                R.layout.slide_five,
                R.layout.slide_six};

        addBottomDots(0);
        changeStatusBarColor();
        getUserAccountDetails();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        //  int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        //  int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (Build.VERSION.SDK_INT >= 24) {
                dots[i].setText(Html.fromHtml("•", Html.FROM_HTML_MODE_LEGACY));
            } else {
                //dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setText(Html.fromHtml("•"));
            }
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.wt_circle));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.primary_color));
            dots[currentPage].setTextSize(40);
        }
    }

    @Click(R.id.btn_skip)
    void skipClicks() {
        Util.setFirstOpened(context, "");
        sendStatus();
        if (Util.isReferOpened(this))
            ReferAndWinActivity_.intent(this).start();
        else if (Util.isSuperPointsOpened(this))
            SuperPointsActivity_.intent(this).start();
        finish();

    }

    @Click(R.id.btn_next)
    void nextClicks() {
        int current = getItem(+1);
        if (current < layouts.length) {
            viewPager.setCurrentItem(current);
        } else {
            Util.setFirstOpened(context, "");
            sendStatus();
            if (Util.isReferOpened(this))
                ReferAndWinActivity_.intent(this).start();
            else if (Util.isSuperPointsOpened(this))
                SuperPointsActivity_.intent(this).start();
            finish();
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                buttonNext.setText(R.string.welcome_start);
                buttonSkip.setVisibility(View.GONE);
            } else {
                buttonNext.setText(R.string.welcome_next);
                buttonSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            FontUtils.setDefaultFont(context, view);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            wtDevices(imageView);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void wtDevices(ImageView imageView) {
        int density = imageView.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                layoutParams1.setMargins(0, 25, 0, 0);
                imageView.setLayoutParams(layoutParams1);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 450);
                layoutParams.setMargins(0, 38, 0, 0);
                imageView.setLayoutParams(layoutParams);
                break;
        }
    }

    private void getUserAccountDetails() {
        Call<APIResponse<AccountDetails>> callback = apiService.getAccountDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<AccountDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<AccountDetails>> response) {
                try {
                    if(response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(WelcomeActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(WelcomeActivity.this, response.body().getValues());
                                    } else {
                                        Util.makeToast(WelcomeActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(WelcomeActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(WelcomeActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(WelcomeActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void sendStatus() {
        Call<APIResponse> callback = apiService.userOepened(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(WelcomeActivity.this, user);
                                sendStatus();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {

                            }
                        }
                    } else {
                        Util.makeToast(WelcomeActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(WelcomeActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("JSONError", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
