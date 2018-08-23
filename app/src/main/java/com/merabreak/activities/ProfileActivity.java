package com.merabreak.activities;

import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.merabreak.ArcView;
import com.merabreak.ArcViewOne;
import com.merabreak.ArcViewThree;
import com.merabreak.ArcViewTwo;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.google.android.gms.common.api.GoogleApiClient;
import com.merabreak.models.PointsWallet;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Ewebcore on 14-Jan-16.
 */
@EActivity(R.layout.profile)
public class ProfileActivity extends BaseActivity {

    private ImageLoader mImageLoader;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.profile_image)
    ImageView image;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.seekbar_rank)
    SeekBar mSeekbarRank;

    @ViewById(R.id.seekbar_challanges)
    SeekBar mSeekbarChallanges;

    @ViewById(R.id.seekbar_coin_won)
    SeekBar mSeekbarCoinWon;

    @ViewById(R.id.seekbar_referrals)
    SeekBar mSpacingReferrals;

    @ViewById(R.id.seekbar_raffles)
    SeekBar mSeekbarRaffles;

    @ViewById(R.id.target_challenges)
    TextView targetChallenges;

    @ViewById(R.id.target_coins)
    TextView targetCoins;

    @ViewById(R.id.target_referrals)
    TextView targetReferrals;

    @ViewById(R.id.target_raffles)
    TextView targetRaffles;

    @ViewById(R.id.current_rank)
    TextView currentRank;

    @ViewById(R.id.target_rank)
    TextView targetRank;

    @ViewById(R.id.rank)
    TextView rank;

    @ViewById(R.id.tot_points_num)
    TextView labelCoins;

    @ViewById(R.id.tot_raffles_num)
    TextView labelRaffles;

    @ViewById(R.id.tot_chall_num)
    TextView labelChallenges;

    @ViewById(R.id.tot_orders_num)
    TextView labelOrders;

    @ViewById(R.id.tot_deals_num)
    TextView labelDeals;

    @ViewById(R.id.circularProgressbar)
    ProgressBar circularProgressbar;

    @ViewById(R.id.circularProgressbar1)
    ProgressBar circularProgressbar1;

    @ViewById(R.id.circularProgressbar2)
    ProgressBar circularProgressbar2;

    @ViewById(R.id.circularProgressbar3)
    ProgressBar circularProgressbar3;

    @ViewById(R.id.arc_progress)
    ArcView arcProgress;

    @ViewById(R.id.arc_progress_one)
    ArcViewOne arcProgressOne;

    @ViewById(R.id.arc_progress_two)
    ArcViewTwo arcProgressTwo;

    @ViewById(R.id.arc_progress_three)
    ArcViewThree arcProgressThree;

    @ViewById(R.id.tv)
    TextView tv;

    @ViewById(R.id.arc_one_text)
    TextView arc_one_text;

    @ViewById(R.id.arc_two_text)
    TextView arc_two_text;

    @ViewById(R.id.arc_three_text)
    TextView arc_three_text;

    @ViewById(R.id.arc_four_text)
    TextView arc_four_text;

    @ViewById(R.id.tv1)
    TextView tv1;

    @ViewById(R.id.tv2)
    TextView tv2;

    @ViewById(R.id.tv3)
    TextView tv3;

    @ViewById(R.id.cha_tot)
    TextView cha_tot;

    @ViewById(R.id.ref_tot)
    TextView ref_tot;

    @ViewById(R.id.poi_tot)
    TextView poi_tot;

    @ViewById(R.id.raff_tot)
    TextView raff_tot;

    @ViewById(R.id.cha_text)
    TextView cha_text;

    @ViewById(R.id.cha_text1)
    TextView cha_text1;

    @ViewById(R.id.cha_text2)
    TextView cha_text2;

    @ViewById(R.id.cha_text3)
    TextView cha_text3;

    @ViewById(R.id.user_mobile)
    TextView userMobile;

    @ViewById(R.id.email_view)
    View emailView;

    @ViewById(R.id.email)
    RelativeLayout email;

    @ViewById(R.id.user_email)
    TextView userEmail;

    @ViewById(R.id.dob_view)
    View dobView;

    @ViewById(R.id.dob)
    RelativeLayout dob;

    @ViewById(R.id.user_dob)
    TextView userDoB;

    @ViewById(R.id.gender_view)
    View genderView;

    @ViewById(R.id.gender)
    RelativeLayout gender;

    @ViewById(R.id.user_gender)
    TextView userGender;

    @ViewById(R.id.location_view)
    View locationView;

    @ViewById(R.id.location)
    RelativeLayout location;

    @ViewById(R.id.user_location)
    TextView userLocation;

    @ViewById(R.id.city_view)
    View cityView;

    @ViewById(R.id.city)
    RelativeLayout city;

    @ViewById(R.id.user_city)
    TextView userCity;

    @ViewById(R.id.zip_view)
    View zipView;

    @ViewById(R.id.zipcode)
    RelativeLayout zipCode;

    @ViewById(R.id.user_zipcode)
    TextView userZipCode;

    @ViewById(R.id.about)
    RelativeLayout about;

    @ViewById(R.id.user_about)
    TextView userAbout;

    int coinWon, referrals;
    private GoogleApiClient client;

    @AfterViews
    void init() {
        title.setVisibility(View.GONE);
        mImageLoader = new ImageLoader(this);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        toolbar.inflateMenu(R.menu.profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        if (Util.isUserLoggedIn(context)) {
                            EditProfileActivity_.intent(ProfileActivity.this).start();

                            return true;
                        } else {
                            Util.makeToast(ProfileActivity.this, getResources().getString(R.string.profile_not_merabreak_user));
                        }
                }
                return false;
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 21)
            Util.changeSystemBarColor(this, R.color.white_transparent);

        AccountDetails ad = Util.getUserAccountDetails(this);
        setAccountDetails(ad);

        getUserAccountDetails();
    }

    private void setData(){
        userMobile.setText(user.getPhone());
        userEmail.setText(user.getEmail());

        if (user.getDob() != null && !user.getDob().equalsIgnoreCase("")){
            dob.setVisibility(View.VISIBLE);
            dobView.setVisibility(View.VISIBLE);
            userDoB.setText(user.getDob());
        } else {
            dob.setVisibility(View.GONE);
            dobView.setVisibility(View.GONE);
        }

        if (user.getGender() != null && !user.getGender().equalsIgnoreCase("")){
            gender.setVisibility(View.VISIBLE);
            genderView.setVisibility(View.VISIBLE);
            userGender.setText(user.getGender());
        } else {
            gender.setVisibility(View.GONE);
            genderView.setVisibility(View.GONE);
        }

        if (user.getLocation() != null && !user.getLocation().equalsIgnoreCase("")){
            location.setVisibility(View.VISIBLE);
            locationView.setVisibility(View.VISIBLE);
            userLocation.setText(user.getLocation());
        } else {
            location.setVisibility(View.GONE);
            locationView.setVisibility(View.GONE);
        }

        if (user.getSelectedCity() != null && !user.getSelectedCity().equalsIgnoreCase("")){
            city.setVisibility(View.VISIBLE);
            cityView.setVisibility(View.VISIBLE);
            userCity.setText(user.getSelectedCity());
        } else {
            city.setVisibility(View.GONE);
            cityView.setVisibility(View.GONE);
        }

        if (user.getZipCode() != null && !user.getZipCode().equalsIgnoreCase("")){
            zipCode.setVisibility(View.VISIBLE);
            zipCode.setVisibility(View.VISIBLE);
            userZipCode.setText(user.getZipCode());
        } else {
            zipCode.setVisibility(View.GONE);
            zipView.setVisibility(View.GONE);
        }

        if (user.getAbout() != null && !user.getAbout().equalsIgnoreCase("")){
            userAbout.setText(user.getAbout());
            about.setVisibility(View.VISIBLE);
        } else {
            about.setVisibility(View.GONE);
        }

        if(user.getDob() == null && user.getDob().equalsIgnoreCase("") &&
                user.getGender() == null && user.getGender().equalsIgnoreCase("") &&
                user.getLocation() == null && user.getLocation().equalsIgnoreCase("") &&
                user.getCity() == null && user.getCity().equalsIgnoreCase("") &&
                user.getZipCode() == null && user.getZipCode().equalsIgnoreCase("") &&
                user.getAbout() == null && user.getAbout().equalsIgnoreCase("")){
            emailView.setVisibility(View.GONE);
        }
    }

    private void getUserAccountDetails() {
        //  hideLoader();
        // showLoader();
        Call<APIResponse<AccountDetails>> callback = apiService.getAccountDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<AccountDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<AccountDetails>> response) {
                // hideLoader();
                try {
                    if(response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(ProfileActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(ProfileActivity.this, response.body().getValues());
                                        setAccountDetails(response.body().getValues());

                                        PointsWallet pointsWallet = Util.getPointsWallet(ProfileActivity.this);
                                        pointsWallet.setTotal_points(String.valueOf(response.body().getValues().getCoinBalance()));

                                        Util.setPointWallet(ProfileActivity.this,pointsWallet);

                                    } else {
                                        Util.makeToast(ProfileActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(ProfileActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(ProfileActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(ProfileActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                // hideLoader();
            }
        });
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }

    private void setAccountDetails(AccountDetails ad) {
        if (ad == null)
            return;
        labelChallenges.setText(String.valueOf(ad.getPlayedChallenge()));
        labelOrders.setText(String.valueOf(ad.getTotalProducts()));
        labelCoins.setText(doubleToStringNoDecimal(ad.getCoinBalance()));
        labelRaffles.setText(String.valueOf(ad.getTotalRaffles()));
        labelDeals.setText(String.valueOf(ad.getTotalDeals()));
        cha_tot.setText(getResources().getString(R.string.profile_total) + " " +  ad.getTargetChallenges()+"");

        if (ad.getTargetCoins() < 10000)
            poi_tot.setText(getResources().getString(R.string.profile_total) + " " + ad.getTargetCoins() + "");
        else
            poi_tot.setText(getResources().getString(R.string.profile_total) + " " + format(ad.getTargetCoins()) + "");

        ref_tot.setText(getResources().getString(R.string.profile_total) + " " +  ad.getTargetReferrals() + "");
        raff_tot.setText(getResources().getString(R.string.profile_total) + " " + + ad.getTargetRaffles() + "");
        currentRank.setText(ad.getCurrentRank());
        targetRank.setText(ad.getTargetRank());

        rank.setText(ad.getCurrentRank());

        coinWon = ad.getCoinsWon();
        referrals = ad.getReferrals();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        cha_text.setTypeface(font);
        cha_text1.setTypeface(font);
        cha_text2.setTypeface(font);
        cha_text3.setTypeface(font);

        addSeekbar(ad);
    }

    private final char[] SUFFIXES = {'K', 'M', 'G'};

    public String format(long number) {
        // Convert to a string
        final String string = String.valueOf(number);
        // The suffix we're using, 1-based
        final int magnitude = (string.length() - 1) / 3;
        // The number of digits we must show before the prefix
        final int digits = (string.length() - 1) % 3 + 1;
        // Build the string
        char[] value = new char[4];
        for (int i = 0; i < digits; i++) {
            value[i] = string.charAt(i);
        }
        int valueLength = digits;
        // Can and should we add a decimal point and an additional number?
        if (digits == 1 && string.charAt(1) != '0') {
            value[valueLength++] = '.';
            value[valueLength++] = string.charAt(1);
        }
        value[valueLength++] = SUFFIXES[magnitude - 1];
        return new String(value, 0, valueLength);
    }

    public void addSeekbar(AccountDetails ad) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        arcProgress.setMax(ad.getTargetChallenges());
        arcProgress.setProgress(ad.getPlayedChallenge());
        arc_one_text.setText(String.valueOf(ad.getPlayedChallenge()));
        arc_one_text.setTypeface(font);
        // arcProgress.setBottomText(String.valueOf(ad.getPlayedChallenge()));
        //  arcProgress.setBottomTextSize(48);

        arcProgressOne.setMax(ad.getTargetReferrals());
        arcProgressOne.setProgress(referrals);
        arc_two_text.setText(String.valueOf(referrals));
        arc_two_text.setTypeface(font);
        //arcProgressOne.setBottomText(String.valueOf(referrals));
        // arcProgressOne.setBottomTextSize(48);

        arcProgressTwo.setMax(ad.getTargetCoins());
        arcProgressTwo.setProgress(coinWon);
        arc_three_text.setTypeface(font);
        if (coinWon < 1000)
            arc_three_text.setText(String.valueOf(coinWon));
        else
            arc_three_text.setText(String.valueOf(format(coinWon)));
        //   if (coinWon < 1000)
        //        arcProgressTwo.setBottomText(String.valueOf(coinWon));
        //    else
        //        arcProgressTwo.setBottomText(String.valueOf(format(coinWon)));
        //    arcProgressTwo.setBottomTextSize(48);

        arcProgressThree.setMax(ad.getTargetRaffles());
        arcProgressThree.setProgress(ad.getTotalRaffles());
        arc_four_text.setText(String.valueOf(ad.getTotalRaffles()));
        arc_four_text.setTypeface(font);
        //  arcProgressThree.setBottomText(String.valueOf(ad.getTotalRaffles()));
        //    arcProgressThree.setBottomTextSize(48);

        circularProgressbar.setMax(ad.getTargetChallenges());
        circularProgressbar.setProgress(ad.getPlayedChallenge());
        tv.setText(String.valueOf(ad.getPlayedChallenge()));

        circularProgressbar1.setMax(ad.getTargetReferrals());
        circularProgressbar1.setProgress(referrals);
        tv1.setText(String.valueOf(referrals));

        circularProgressbar2.setMax(ad.getTargetCoins());
        circularProgressbar2.setProgress(coinWon);

        if (coinWon < 10000)
            tv2.setText(String.valueOf(coinWon));
        else
            tv2.setText(String.valueOf(format(coinWon)));

        circularProgressbar3.setMax(ad.getTargetRaffles());
        circularProgressbar3.setProgress(ad.getTotalRaffles());
        tv3.setText(String.valueOf(ad.getTotalRaffles()));

        mSeekbarChallanges.setMax(ad.getTargetChallenges());
        mSeekbarChallanges.setProgress(ad.getPlayedChallenge());

        mSeekbarCoinWon.setMax(ad.getTargetCoins());
        mSeekbarCoinWon.setProgress(coinWon);

        mSpacingReferrals.setMax(ad.getTargetReferrals());
        mSpacingReferrals.setProgress(referrals);

        mSeekbarRaffles.setMax(ad.getTargetRaffles());
        mSeekbarRaffles.setProgress(ad.getTotalRaffles());

        mSeekbarRank.setMax(ad.getTargetChallenges() + ad.getTargetCoins() + ad.getTargetReferrals() + ad.getTargetRaffles());
        int total = ad.getPlayedChallenge() + coinWon + referrals + ad.getTotalRaffles();
        mSeekbarRank.setProgress(total);

        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(1);
        thumb.setIntrinsicWidth(1);

        mSeekbarRank.setThumb(thumb);
        mSeekbarChallanges.setThumb(thumb);
        mSeekbarCoinWon.setThumb(thumb);
        mSpacingReferrals.setThumb(thumb);
        mSeekbarRaffles.setThumb(thumb);

        mSeekbarRank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mSeekbarChallanges.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mSeekbarCoinWon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mSpacingReferrals.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mSeekbarRaffles.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Click(R.id.tot_orders)
    void onOders() {
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(3).start();
    }

    @Click(R.id.tot_challenges)
    void onChallenges() {
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(1).start();
    }

    @Click(R.id.tot_points)
    void onCoins() {
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(0).start();
    }

    @Click(R.id.tot_raffles)
    void onRaffles() {
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(2).start();
    }

    @Click(R.id.tot_deals)
    void onDeals() {
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(4).start();
    }

    @Click(R.id.addresses)
    void onAddresses() {
        ShippingAddressListActivity_.intent(ProfileActivity.this).start();
    }

    @Click(R.id.show_more)
    void showHistory(){
        AccountDetailsActivity_.intent(ProfileActivity.this).tabId(0).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null) {
            if (user.getImage() != null) {

                Picasso.with(context).load(user.getImage()).placeholder(R.drawable.dummy_profile_image).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                });
            }
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(user.getFullName());
            collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.selected_tab_color));
            // Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat_Regular.ttf");
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
            collapsingToolbar.setExpandedTitleTypeface(font);
            collapsingToolbar.setCollapsedTitleTypeface(font);
            setData();
            getUserAccountDetails(); // by swati
        }
    }
}
