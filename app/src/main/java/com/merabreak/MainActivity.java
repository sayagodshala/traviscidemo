package com.merabreak;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.merabreak.activities.ArticleActivity_;
import com.merabreak.activities.ChallengeDetailActivity_;
import com.merabreak.activities.ChallengesActivity_;
import com.merabreak.activities.EditProfileActivity_;
import com.merabreak.activities.GaneshaSpeaksActivity_;
import com.merabreak.activities.NotificationActivity_;
import com.merabreak.activities.OfflineChallengesActivity_;
import com.merabreak.activities.ProfileActivity_;
import com.merabreak.activities.RaffleActivity_;
import com.merabreak.activities.RaffleContestActivity_;
import com.merabreak.activities.RafflesActivity_;
import com.merabreak.activities.RechargeActivity_;
import com.merabreak.activities.RedeemPointsActivity_;
import com.merabreak.activities.ReferAndWinActivity_;
import com.merabreak.activities.ScanActivity_;
import com.merabreak.activities.SettingsActivity_;
import com.merabreak.activities.SpinWheelActivity_;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.activities.StoreActivity_;
import com.merabreak.activities.StoresActivity_;
import com.merabreak.activities.WelcomeActivity_;
import com.merabreak.activities.WinnersGalleryActivity_;
import com.merabreak.adapter.FeaturedChallengeSliderView;
import com.merabreak.adapter.FeaturedChallengeSliderViewBanner;
import com.merabreak.controls.FragmentAdapter;
import com.merabreak.db.DbHelper;
import com.merabreak.fragments.AstrologyFragment;
import com.merabreak.fragments.AstrologyFragment_;
import com.merabreak.fragments.AutoFragment;
import com.merabreak.fragments.AutoFragment_;
import com.merabreak.fragments.DrawerFragment;
import com.merabreak.fragments.DrawerFragment_;
import com.merabreak.fragments.EntertainmentFragment;
import com.merabreak.fragments.EntertainmentFragment_;
import com.merabreak.fragments.FashionFragment;
import com.merabreak.fragments.FashionFragment_;
import com.merabreak.fragments.FitnessFragment;
import com.merabreak.fragments.FitnessFragment_;
import com.merabreak.fragments.NatureFragment;
import com.merabreak.fragments.NatureFragment_;
import com.merabreak.fragments.FoodFragment;
import com.merabreak.fragments.FoodFragment_;
import com.merabreak.fragments.HomeFargment;
import com.merabreak.fragments.HomeFargment_;
import com.merabreak.fragments.PeopleFragment;
import com.merabreak.fragments.PeopleFragment_;
import com.merabreak.fragments.SportsFragment;
import com.merabreak.fragments.SportsFragment_;
import com.merabreak.fragments.TechFragment;
import com.merabreak.fragments.TechFragment_;
import com.merabreak.fragments.TravelFragment;
import com.merabreak.fragments.TravelFragment_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.DeepLinkParams;
import com.merabreak.models.DownloadedChallenges;
import com.merabreak.models.GooglePrediction;
import com.merabreak.models.HomeCategory;
import com.merabreak.models.Points;
import com.merabreak.models.PointsWallet;
import com.merabreak.models.ProfileDetails;
import com.merabreak.models.SpinWheelDetails;
import com.merabreak.models.StripParams;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Memimages;
import com.merabreak.models.challenge.Step;
import com.merabreak.network.Connectivity;
import com.merabreak.network.ConnectivityReceiver;
import com.merabreak.network.GoogleAPIClient;
import com.merabreak.network.GoogleAPIResponse;
import com.merabreak.network.GoogleAPIService;
import com.merabreak.pushNotifications.NotificationPreferences;
import com.merabreak.pushNotifications.NotificationType;
import com.merabreak.pushNotifications.RegistrationIntentService;
import com.merabreak.pushNotifications.events.PushType;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
/*import io.deepstream.MergeStrategy;
import io.deepstream.Record;
import io.deepstream.RecordPathChangedCallback;*/
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EActivity(R.layout.main)
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static String TAG = MainActivity.class.getName();
    private static final String NAVIGATION_ITEM_TAG = "NAVIGATION_ITEM_TAG";
    private static final String NAVIGATION_DRAWER_TAG = "NAVIGATION_DRAWER_TAG";

    PointsWallet pointsWallet;

    LinearLayout dobPickerHldr;
    TextView profileTitle;
    TextView profileUpdateDesc;

    DatePicker dobDatepicker;

    @ViewById(R.id.drawer)
    DrawerLayout drawer;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.pointsNum)
    TextView pointsNum;

    @ViewById(R.id.single_challenge_image)
    ImageView singleImage;

    @ViewById(R.id.single_challenge_download)
    ImageView singleDownload;

    @ViewById(R.id.overflow_menu)
    ImageView overflow_menu;

    @ViewById(R.id.spin_icon)
    ImageView spin_icon;

    @ViewById(R.id.notification_count_hldr)
    RelativeLayout notificationHldr;

    @ViewById(R.id.points_icon)
    ImageView points_icon;

    @ViewById(R.id.strip_image)
    ImageView strip_image;

    @ViewById(R.id.strip_image_slider)
    ImageView strip_image_slider;

    @ViewById(R.id.single_challenge_title)
    TextView singleTitle;

    @ViewById(R.id.single_challenge_category)
    TextView singleLabel;

    @ViewById(R.id.single_challenge_total_plays)
    TextView singlePlays;

    @ViewById(R.id.home_no_internet)
    LinearLayout home_no_internet;

    @ViewById(R.id.carousel)
    RelativeLayout carousel;

    @ViewById(R.id.home_tap)
    TextView home_tap;

    @ViewById(R.id.single_challenge)
    RelativeLayout single_challenge;

    @ViewById(R.id.single_challenge_main)
    RelativeLayout single_challenge_main;

    List<String> downloadURLs = new ArrayList<>();
    private int totalCounts = 0;
    private int finalCounts = 0;

    ActionBarDrawerToggle drawerToggle;

    @ViewById(R.id.tabs)
    TabLayout mTabLayout;

    @ViewById(R.id.viewpager)
    ViewPager viewpager;

    @ViewById(R.id.slider)
    SliderLayout slider;

    @ViewById(R.id.banner_main)
    RelativeLayout banner;

    @ViewById(R.id.custom_indicator)
    PagerIndicator customIndicator;

    @ViewById(R.id.spin_count)
    TextView spinCount;

    @ViewById(R.id.notification_count)
    TextView notificationCount;

    @ViewById(R.id.ipl_layout)
    LinearLayout ipl_layout;

    @ViewById(R.id.ipl_days)
    TextView ipl_days;

    @ViewById(R.id.ipl_hours)
    TextView ipl_hours;

    @ViewById(R.id.ipl_minutes)
    TextView ipl_minutes;

    @ViewById(R.id.ipl_seconds)
    TextView ipl_seconds;

    @ViewById(R.id.prizes_gif)
    ImageView prizesGif;

    @ViewById(R.id.fab)
    FloatingActionButton fabIcon;

    List<Fragment> tabsFragments = new ArrayList<>();
    public List<Challenge> fc = new ArrayList<Challenge>();
    List<Points> allPoints = new ArrayList<Points>();
    AlertDialog alertDialog;

    String stripImage, utmContent, totPoints, proPoints, superPoints;
    int stripFlag, spin_flag, spin_waiting_time, flagMCL;
    long iplCountDown;
    SpinWheelDetails spinWheelDetails;
    private String gender = "";

    @Extra
    String serachLocatioReq;

    private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //private static final int MY_PERMISSION_CONTACTS = 101;
    private static final int MY_PERMISSION_PHONE = 101;
    TelephonyManager manager;
    String deviceId = "not given phone state permission";
    String carrierName = "not given phone state permission";

    ArrayList<String> contacts = new ArrayList<String>();
    List<HomeCategory> homeCategories = new ArrayList<HomeCategory>();
    List<Challenge> homeBannerContent = new ArrayList<Challenge>();

    @Extra
    DeepLinkParams deepLinkParams;
    private boolean isHeartBeatLoaded = false;
    GoogleAPIService googleAPIService;
    private CustomListAdapter projectItemsAdapter = null;
    /*private ChallengeContestCallBack challengeContestCallBack;
    private Record deepStreamRecordChallengeContest;*/

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            return;
        }
        exitAppAlert();
    }

    private void exitAppAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context.getResources().getString(R.string.main_activity_exit_app)).setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
            finish();
        }).setNegativeButton(context.getResources().getString(R.string.no), null); //Do nothing on no
        AlertDialog alert = builder.create();
        alert.show();
        FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    @AfterViews
    protected void registerPushToken() {
        if (serachLocatioReq == null) {
            if (checkPlayServices()) {
                startService(new Intent(this, RegistrationIntentService.class));
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(MainActivity.this, this.getResources().getString(R.string.main_activity_play_not_found), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    private void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context.getResources().getString(R.string.main_activity_logout)).setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        }).setNegativeButton(context.getResources().getString(R.string.no), null);                        //Do nothing on no
        AlertDialog alert = builder.create();
        alert.show();
        FontUtils.setDefaultFont(this, alert.findViewById(android.R.id.content));
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(this).clearTable();
        SharedPreferences settings = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SplashActivity_.intent(this).start();
        finish();
    }

    private void logout() {
        String token = NotificationPreferences.prefs(this).getString(NotificationPreferences.NOTIFICATION_REGISTRATION_TOKEN, "");
        //Log.d("logout token", token);
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.logout(Constants.MB_API_KEY, user.getAuthToken(), user.getPhone(), token);
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(MainActivity.this, user);
                                logout();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                GoogleAnalyticEvents.event(MainActivity.this, gaTracker, "Logout", "User Logged Out from the app", user.getFullName() + ": " + user.getPhone() + ": " + new Date().toString());
                               /* Map<String, Object> eventValue = new HashMap<String, Object>();
                                eventValue.put(AFInAppEventParameterName.DATE_B, new Date().toString());
                                ((ApplicationSingleton) MainActivity.this.getApplication()).appsFlyerLib().trackEvent(MainActivity.this, "Logout", eventValue);*/
                                Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                clearAllUserDataAndFinish();
                                OneSignal.setSubscription(false);
                            } else {
                                Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t.getMessage() != null) {
                    Log.d("JSONError", t.getMessage());
                    Util.makeToast(MainActivity.this, t.getMessage());
                } else {
                    Log.d("API Error", Constants.SOME_THING_WRONG);
                }
            }
        });
    }

    @AfterViews
    void initDrawer() {
        drawerToggle =
                new ActionBarDrawerToggle(this, drawer, null, R.string.app_name, R.string.app_name);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
    }

    @AfterViews
    void init() {
        //String uri = "https://thumbs.gfycat.com/SameGlitteringBengaltiger-size_restricted.gif";
        /*challengeContestCallBack = new ChallengeContestCallBack();
        revokeDeepStreamRecordForChallengeContest();
        setDeepStreamRecordForChallengeContest();*/
        Glide.with(this).load(R.raw.ganesha_speaks).into(prizesGif);
        navigationItemSelected(NavigationItemType.HOME);
        GoogleAnalyticEvents.eventAppLoaded(this, gaTracker, "User Opened The App", user.getFullName() + ": " + user.getPhone() + ": " + new Date().toString());
        /*Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.DATE_A, new Date().toString());
        ((ApplicationSingleton) MainActivity.this.getApplication()).appsFlyerLib().trackEvent(MainActivity.this, "User Open The App", eventValue);*/

        FontUtils.setDefaultFont(this, findViewById(android.R.id.content));
        OneSignal.setSubscription(true);
        if (user.getEmail() != null) {
            OneSignal.syncHashedEmail(user.getEmail());
            OneSignal.sendTag("Email", user.getEmail());
        }
        OneSignal.sendTag("Mobile number", user.getPhone());
        if (user.getFullName() != null)
            OneSignal.sendTag("Full name", user.getFullName());
        //Animation shake = AnimationUtils.loadAnimation(this, R.anim.zoom_in_new);
        // pointsNum.startAnimation(shake);     q   `       `

        if (user.getDataEnabled() == 1)
            Util.setOffline(MainActivity.this, "true");
        else
            Util.setOffline(MainActivity.this, "");


        // Ask user to update your profile
        SharedPreferences preferences = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();

        if(preferences.getBoolean(AppSettings.PREF_IS_UPDATE_PROFILE_DIALOG_VISIBLE,false) == false) {
            getProfileDetails();
        }


        loadDrawer();
        requestPermissions();
        //getStripDetails();

        title.setText(R.string.app_name);

        toolbar.setNavigationOnClickListener(v -> toggleDrawer());
        toolbar.inflateMenu(R.menu.main);
        setFontToMenu();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_account:
                        ProfileActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.settings:
                        SettingsActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.raffles:
                        RafflesActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.stores:
                        StoresActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.rate_us:
                        return true;
                    case R.id.qr_code:
                        ScanActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.logout:
                        logoutAlert();
                        return true;
                    case R.id.redeem:
                        RedeemPointsActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.offline_chall:
                        OfflineChallengesActivity_.intent(MainActivity.this).start();
                        return true;
                    case R.id.winners:
                        WinnersGalleryActivity_.intent(MainActivity.this).start();
                        return true;
                    default:
                        return false;
                }
            }
        });
        if (user != null) {
            Crashlytics.setUserIdentifier(user.getAuthToken());
            Crashlytics.setUserEmail(user.getEmail());
            Crashlytics.setUserName(user.getFullName());
        }

        if (deepLinkParams != null && deepLinkParams.getAction() != null)
            if (deepLinkParams.getAction().equalsIgnoreCase("open_category")) {

            } else if (deepLinkParams.getAction().equalsIgnoreCase("open_challenge")) {
                ChallengeDetailActivity_.intent(this).slug(deepLinkParams.getSlug()).start();
            } else if (deepLinkParams.getAction().equalsIgnoreCase("open_raffle")) {
                RaffleActivity_.intent(this).id(deepLinkParams.getSlug()).start();
            } else if (deepLinkParams.getAction().equalsIgnoreCase("open_product")) {
                StoreActivity_.intent(this).id(deepLinkParams.getSlug()).start();
            }

        utmContent = Util.getCampaignContent(this);
        if (!utmContent.equalsIgnoreCase("")) {
            ChallengeDetailActivity_.intent(this).slug(utmContent).start();
            Util.saveCampaignContent(this, "");
        }
    }

    private void checkConnectivity() {
        if (ConnectivityReceiver.isConnected(this)) {
            if (!isHeartBeatLoaded)
                getHeartBeat();
            getUserAccountDetails();
            getWalletDetails();
        } else {
            carousel.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
            home_no_internet.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.play_off_cha)
    void playOffChallenges() {
        OfflineChallengesActivity_.intent(this).start();
    }


    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d);
    }

    private void walletPointsAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.point_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView dialod_title = (TextView) promptView.findViewById(R.id.dialod_title);
        TextView wallet_points_title = (TextView) promptView.findViewById(R.id.wallet_points_title);
        TextView wallet_super_points_title = (TextView) promptView.findViewById(R.id.wallet_super_points_title);
        dialod_title.setTypeface(font);
        wallet_points_title.setTypeface(font);
        wallet_super_points_title.setTypeface(font);
        TextView wallet_points = (TextView) promptView.findViewById(R.id.wallet_points);
        TextView wallet_super_points = (TextView) promptView.findViewById(R.id.wallet_super_points);
        Button wallet_redeem = (Button) promptView.findViewById(R.id.wallet_redeem);
        Button wallet_close = (Button) promptView.findViewById(R.id.wallet_close);
        wallet_redeem.setTypeface(font);
        if (totPoints != null) {
            wallet_points.setText(doubleToStringNoDecimal(Integer.parseInt(totPoints)));
        } else {
            wallet_points.setText("0");
        }
        if (proPoints != null) {
            wallet_super_points.setText(doubleToStringNoDecimal(Integer.parseInt(proPoints)));
        } else {
            wallet_super_points.setText("0");
        }
        AlertDialog alertDialog = builder.create();
        wallet_redeem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RedeemPointsActivity_.intent(MainActivity.this).start();
                alertDialog.dismiss();
            }
        });
        wallet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void loadDrawer() {
        DrawerFragment drawer = DrawerFragment_.builder().build();
        drawer.navigationItemSelected().subscribe(this::navigationItemSelected);
        Fragments.replaceContentFragment(this, R.id.drawer_container, drawer, NAVIGATION_DRAWER_TAG);
    }

    private void navigationItemSelected(final NavigationItemType navigationItemType) {
        closeDrawer();
    }

    private void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getStringExtra("NOTI_DATA") != null) {
            onHandleIntent(intent);
        }
    }

    private void onHandleIntent(Intent intent) {
        String data = intent.getStringExtra("NOTI_DATA");
        PushType pushType = new Gson().fromJson(data, new TypeToken<PushType>() {
        }.getType());

        NotificationType type = NotificationType.fromInt(Integer.parseInt(pushType.getType()));

        if (type == NotificationType.NEW_CHALLENGE) {
            if (((ApplicationSingleton) getApplication()).isChallengePlaying) {
                Utility.cancelNotification(MainActivity.this, 1001);
            } else {
                ChallengesActivity_.intent(MainActivity.this).filterType("New").start();
            }
        } else {
            BusProvider.bus().post(pushType);
        }
        if (type == NotificationType.NEW_OFFER) {

        } else {
            BusProvider.bus().post(pushType);
        }
        if (type == NotificationType.FOR_GUEST) {

        } else {
            BusProvider.bus().post(pushType);
        }
        if (type == NotificationType.REFFERER_REGD) {
            String points = pushType.getDetails().get(0).getCoins();
            pointsNum.setText(points);
            totPoints = points;
            OneSignal.sendTag("Points", points);
        } else {
            BusProvider.bus().post(pushType);
        }
        if (type == NotificationType.RAFFLE_WINNER) {

        } else {
            BusProvider.bus().post(pushType);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {

        if (!hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            appAlert();
        } else {
            deviceId = Util.uniqueDeviceID(this);
            manager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null && manager.getNetworkOperatorName() != null) {
                carrierName = manager.getNetworkOperatorName();
            }
            try {
                String latLng = Util.getLatLng(MainActivity.this);
                String cityId = Util.getSelectedCityId(MainActivity.this);
                if (serachLocatioReq != null) {
                    deviceInfoUpdate(new JSONObject(serachLocatioReq));
                    serachLocatioReq = null;
                } else {
                    if (!latLng.equalsIgnoreCase("")) {
                        deviceInfoUpdate(new JSONObject().put("latLng", latLng));
                    } else if (!cityId.equalsIgnoreCase("")) {
                        deviceInfoUpdate(new JSONObject().put("cityId", cityId));
                    } else {
                        deviceInfoUpdate(new JSONObject());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void appAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context.getResources().getString(R.string.mobile_verifi_perm_app_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermissions();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.selected_tab_color));
    }

    private void checkPermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        deviceId = Util.uniqueDeviceID(MainActivity.this);
                        manager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                        if (manager != null && manager.getNetworkOperatorName() != null) {
                            carrierName = manager.getNetworkOperatorName();
                        }
                        try {
                            String latLng = Util.getLatLng(MainActivity.this);
                            String cityId = Util.getSelectedCityId(MainActivity.this);
                            if (serachLocatioReq != null) {
                                deviceInfoUpdate(new JSONObject(serachLocatioReq));
                                serachLocatioReq = null;
                            } else {
                                if (!latLng.equalsIgnoreCase(""))
                                    deviceInfoUpdate(new JSONObject().put("latLng", latLng));
                                else if (!cityId.equalsIgnoreCase(""))
                                    deviceInfoUpdate(new JSONObject().put("cityId", cityId));
                                else
                                    deviceInfoUpdate(new JSONObject());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        /* ... */
                        try {
                            String latLng = Util.getLatLng(MainActivity.this);
                            String cityId = Util.getSelectedCityId(MainActivity.this);
                            if (serachLocatioReq != null) {
                                deviceInfoUpdate(new JSONObject(serachLocatioReq));
                                serachLocatioReq = null;
                            } else {
                                if (!latLng.equalsIgnoreCase(""))
                                    deviceInfoUpdate(new JSONObject().put("latLng", latLng));
                                else if (!cityId.equalsIgnoreCase(""))
                                    deviceInfoUpdate(new JSONObject().put("cityId", cityId));
                                else
                                    deviceInfoUpdate(new JSONObject());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        /* ... */
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void deviceInfoUpdate(JSONObject data) {
        String manufacturer = Build.MANUFACTURER;
        String modelNumber = Build.MODEL;
        String osVersion = Build.VERSION.RELEASE;
        String appVersion = String.valueOf(BuildConfig.VERSION_NAME);
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = status.getSubscriptionStatus().getUserId();
        String token = NotificationPreferences.prefs(this).getString(NotificationPreferences.NOTIFICATION_REGISTRATION_TOKEN, "");
        //if (token != null)
        // Log.d("device info token", token);

        Call<APIResponse> callback = null;
        try {
            if (data.has("latLng")) {
                String[] coords = data.getString("latLng").split(",");
                callback = apiService.deviceInfoUpdate(Constants.MB_API_KEY, user.getAuthToken(), token, deviceId, "android", manufacturer, appVersion, osVersion, modelNumber, carrierName, versionCode, "", userId, coords[0], coords[1]);
            } else if (data.has("cityId")) {
                callback = apiService.deviceInfoUpdate(Constants.MB_API_KEY, user.getAuthToken(), token, deviceId, "android", manufacturer, appVersion, osVersion, modelNumber, carrierName, versionCode, "", userId, data.getString("cityId"));
            } else {
                callback = apiService.deviceInfoUpdate(Constants.MB_API_KEY, user.getAuthToken(), token, deviceId, "android", manufacturer, appVersion, osVersion, modelNumber, carrierName, versionCode, "", userId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(MainActivity.this, user);
                                deviceInfoUpdate(data);
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else if (response.body().getMeta().getStatusCode() == 406) {
                            appUpdate();
                        }
                        //loadDrawer();
                        //getHomeBanner();
                        // setupViewPager();
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void getHeartBeat() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.getHeartBeat();
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    isHeartBeatLoaded = true;
                    if (response.body().getMeta().statusCode == 200) {
                        carousel.setVisibility(View.VISIBLE);
                        getHomeBanner();
                        setupViewPager();
                    } else {
                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    private void getHomeBanner() {
        hideLoader(progress);
        showLoader(progress);

        String latLng = Util.getLatLng(MainActivity.this);
        JSONObject data = null;
        String[] coords = null;
        String cityId = null;
        try {
            data = new JSONObject().put("latLng",latLng);
            coords = data.getString("latLng").split(",");
            cityId = Util.getSelectedCityId(MainActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<APIResponse<List<Challenge>>> callback;

        if(coords!=null && coords.length ==2 && cityId != null && !cityId.isEmpty())
        {
            callback = apiService.homeBanner(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this), coords[0], coords[1],cityId);
        }
        else if((coords == null || coords.length !=2) && (cityId != null && !cityId.isEmpty()))
        {
            callback = apiService.homeBanner(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this), "", "",cityId);
        }
        else if((coords != null && coords.length ==2) && (cityId == null || cityId.isEmpty()))
        {
            callback = apiService.homeBanner(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this), coords[0], coords[1],"");
        }
        else
        {
            callback = apiService.homeBanner(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this));
        }

        callback.enqueue(new Callback<APIResponse<List<Challenge>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Challenge>>> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(MainActivity.this, user);
                                getHomeBanner();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null) {
                                    homeBannerContent = response.body().getValues();
                                    setBanner();
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void getStripDetails() {
        Call<APIResponse<StripParams>> callback = apiService.getStripImage(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<StripParams>>() {
            @Override
            public void onResponse(Response<APIResponse<StripParams>> response) {
                hideLoader(progress);
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(MainActivity.this, user);
                                getStripDetails();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues().getStripImage() != null) {
                                    stripImage = response.body().getValues().getStripImage();
                                    stripFlag = response.body().getValues().getStripFlag();
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                }
                            } else {
                                Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setStripImage() {
        if (stripImage != null && stripImage.contains("http")) {
            Picasso.with(context).load(stripImage).fit().centerCrop().placeholder(R.drawable.strip_image_default_compressed).into(strip_image, new com.squareup.picasso.Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        } else {
        }
    }

    private void setSliderStripImage() {
        if (stripImage != null && stripImage.contains("http")) {
            Picasso.with(context).load(stripImage).fit().centerCrop().placeholder(R.drawable.strip_image_default_compressed).into(strip_image_slider, new com.squareup.picasso.Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        } else {
        }
    }

    @Click(R.id.strip_image)
    void stripImage() {
        if (Connectivity.isConnected(context)) {
            if (stripFlag == 1)
                ReferAndWinActivity_.intent(this).start();
            else if (stripFlag == 2)
                RedeemPointsActivity_.intent(this).start();
            else if (stripFlag == 3)
                RafflesActivity_.intent(this).start();
            else if (stripFlag == 4)
                StoresActivity_.intent(this).start();
            else if (stripFlag == 5)
                RechargeActivity_.intent(this).start();
            else if (stripFlag == 6)
                ProfileActivity_.intent(this).start();
            else if (stripFlag == 7)
                OfflineChallengesActivity_.intent(this).start();
            else if (stripFlag == 8) {
                if (spin_flag == 1)
                    SpinWheelActivity_.intent(this).start();
                else
                    spinWaitingTime();
            } else if (stripFlag == 9)
                ScanActivity_.intent(this).start();
            else
                Util.makeToast(MainActivity.this, "Nothing to redirect.Play more challenges and win prizes..!");
        } else {
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_no_internet));
        }
    }

    @Click(R.id.strip_image_slider)
    void stripImageSlider() {
        if (Connectivity.isConnected(context)) {
            if (stripFlag == 1)
                ReferAndWinActivity_.intent(this).start();
            else if (stripFlag == 2)
                RedeemPointsActivity_.intent(this).start();
            else if (stripFlag == 3)
                RafflesActivity_.intent(this).start();
            else if (stripFlag == 4)
                StoresActivity_.intent(this).start();
            else if (stripFlag == 5)
                RechargeActivity_.intent(this).start();
            else if (stripFlag == 6)
                ProfileActivity_.intent(this).start();
            else if (stripFlag == 7)
                OfflineChallengesActivity_.intent(this).start();
            else if (stripFlag == 8) {
                if (spin_flag == 1)
                    SpinWheelActivity_.intent(this).start();
                else
                    spinWaitingTime();
            } else if (stripFlag == 9)
                ScanActivity_.intent(this).start();
            else
                Util.makeToast(MainActivity.this, "Nothing to redirect.Play more challenges and win prizes..!");
        } else {
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_no_internet));
        }
    }

    @Click(R.id.points_icon)
    void showPointsMenu() {
        walletPointsAlert();
    }

    @Click(R.id.spin_icon)
    void spinTheWheel() {
        if (spin_flag == 1)
            getSpinDetails();
        else
            spinWaitingTime();
    }

    @Click(R.id.notification_icon)
    void notificationClick()
    {
        NotificationActivity_.intent(MainActivity.this).start();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

    }

    private void getSpinDetails() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<SpinWheelDetails>> callback = apiService.getSpinDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<SpinWheelDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<SpinWheelDetails>> response) {
                hideLoader(progress);
                try {
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(MainActivity.this, user);
                                    getSpinDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        spinWheelDetails = response.body().getValues();
                                        SpinWheelActivity_.intent(MainActivity.this).spinWheelDetails(spinWheelDetails).start();
                                    } else {
                                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Click(R.id.overflow_menu)
    void showMenu() {
        PopupMenu popup = new PopupMenu(context, overflow_menu);
        popup.inflate(R.menu.options_menu);
        MenuItem item = popup.getMenu().findItem(R.id.download);
        if (homeBannerContent.get(0).getOffline() == 1) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        if (homeBannerContent.get(0).getIsDownloaded() == 1) {
            item.setTitle(context.getResources().getString(R.string.challenge_adapter_downloaded));
        } else {
            item.setTitle(context.getResources().getString(R.string.challenge_adapter_download));
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        if (homeBannerContent.get(0).getIsDownloaded() == 1) {
                            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_already_downloaded));
                        } else {
                            GoogleAnalyticEvents.eventSignup(context, gaTracker, "Challenge Downloaded", "User Downloaded the Challenge", user.getFullName() + ": " + user.getPhone() + ": " + homeBannerContent.get(0).getTitle());
                           /* Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(AFInAppEventParameterName.PARAM_10, homeBannerContent.get(0).getTitle());
                            ((ApplicationSingleton) context.getApplicationContext()).appsFlyerLib().trackEvent(context, "Challenge Downloaded", eventValue);*/
                            if (Connectivity.isConnected(context)) {
                                if (Util.isMobileDataForOfflineDownload(context)) {
                                    if (!Connectivity.isConnectedWifi(context)) {
                                        Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_using_data));
                                    }
                                    saveChallengeOffline();
                                } else {
                                    if (Connectivity.isConnectedWifi(context)) {
                                        saveChallengeOffline();
                                    } else {
                                        Util.makeToast(context, context.getResources().getString(R.string.no_internet));
                                        EditProfileActivity_.intent(context).start();
                                    }
                                }
                            } else {
                                Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_no_internet));
                                //Util.makeToast(mContext, "Ohho!No Internet Connection.Once connected to Internet will automatically download this challenge for you!");
                                //downChallenges.setSlug(mDataset.get(getPosition()).getSlug());
                                // downloadedChallenges.add(downChallenges);
                                // Util.saveDownChalSlugs(mContext, downloadedChallenges);
                            }
                        }
                        break;
                    case R.id.share:
                        shareChallenge();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void shareChallenge() {
        GoogleAnalyticEvents.eventChallengeLike(context, gaTracker, "Challenge Shared", user.getFullName() + ": " + user.getPhone() + ": " + homeBannerContent.get(0).getTitle());
       /* Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_6, homeBannerContent.get(0).getTitle());
        ((ApplicationSingleton) context.getApplicationContext()).appsFlyerLib().trackEvent(context, "Challenge Shared", eventValue);*/
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // .setCanonicalIdentifier("item/12345")
                .setTitle("MeraBreak Challenge")
                //  .setContentDescription("My Content Description")
                //  .setContentImageUrl("https://example.com/mycontent-12345.png")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("action", "open_challenge")
                .addContentMetadata("slug", homeBannerContent.get(0).getSlug());

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing");
        // .addControlParameter("$desktop_url", "http://example.com/home")
        // .addControlParameter("$ios_url", "http://example.com/ios");

        branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    Util.challengeShareApp(MainActivity.this, "MeraBreak", "MeraBreak Challenge - Share Via:", url);
                }
            }
        });
    }

    @Click(R.id.single_challenge_download)
    void singleChallengeDownload() {
        if (Connectivity.isConnected(context)) {
            if (Util.isMobileDataForOfflineDownload(context)) {
                if (!Connectivity.isConnectedWifi(context)) {
                    Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_using_data));
                }
                saveChallengeOffline();
            } else {
                if (Connectivity.isConnectedWifi(context)) {
                    saveChallengeOffline();
                } else {
                    Util.makeToast(context, context.getResources().getString(R.string.no_internet));
                    EditProfileActivity_.intent(context).start();
                }
            }
        } else {
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_no_internet));
        }
    }

    @Click(R.id.single_challenge)
    void openChallenge() {
        GoogleAnalyticEvents.event(context, gaTracker, "Challenge Click", "Challenge Clicked", user.getFullName() + ": " + user.getPhone() + ": " + homeBannerContent.get(0).getTitle());
       /* Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_3, homeBannerContent.get(0).getTitle());
        ((ApplicationSingleton) context.getApplicationContext()).appsFlyerLib().trackEvent(context, "Challenge Clicked", eventValue);*/
        if (homeBannerContent.get(0).getTitle() != null) {
            if (homeBannerContent.get(0).getChallengeType().equalsIgnoreCase("article"))
                ArticleActivity_.intent(context).challenge(homeBannerContent.get(0)).start();
            else
                ChallengeDetailActivity_.intent(context).challenge(homeBannerContent.get(0)).start();
        } else {
            if (homeBannerContent.get(0).getBannerFlag() == 1)
                ReferAndWinActivity_.intent(this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 2)
                RedeemPointsActivity_.intent(this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 3)
                RafflesActivity_.intent(MainActivity.this).slug(homeBannerContent.get(0).getSlug()).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 4)
                StoresActivity_.intent(MainActivity.this).slug(homeBannerContent.get(0).getSlug()).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 5)
                RechargeActivity_.intent(MainActivity.this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 6)
                ProfileActivity_.intent(MainActivity.this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 7)
                OfflineChallengesActivity_.intent(MainActivity.this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 8)
                ChallengeDetailActivity_.intent(MainActivity.this).slug(homeBannerContent.get(0).getSlug()).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 9) {
                if (spin_flag == 1)
                    SpinWheelActivity_.intent(MainActivity.this).start();
                else
                    spinWaitingTime();
            } else if (homeBannerContent.get(0).getBannerFlag() == 10)
                ScanActivity_.intent(MainActivity.this).start();
            else if (homeBannerContent.get(0).getBannerFlag() == 12)
                GaneshaSpeaksActivity_.intent(MainActivity.this).start();
            else
                Util.makeToast(MainActivity.this, "Nothing to redirect.Play more challenges and win prizes..!");
        }
    }

    @Click(R.id.home_tap)
    void checkInternet() {
        if (Connectivity.isConnected(this)) {
            finish();
            startActivity(getIntent());
        } else
            Util.makeToast(context, context.getResources().getString(R.string.main_activity_still_no_internet));

    }

    @Click(R.id.prizes_gif)
    void gotoMCL() {
        //IPLSplashActivity_.intent(this).start();
        GaneshaSpeaksActivity_.intent(MainActivity.this).start();
    }

    private void initBanner() {
        if (slider != null)
            slider.removeAllSliders();
        banner.setVisibility(View.VISIBLE);
        //ipl_layout.setVisibility(View.VISIBLE);
        //setSliderStripImage();
        prizesGif.setVisibility(View.VISIBLE);
        for (int i = 0; i < homeBannerContent.size(); i++) {
            final Challenge challenge = homeBannerContent.get(i);
            if (challenge.getTitle() != null) {
                if (challenge.getChallengeType().equalsIgnoreCase("article"))
                    overflow_menu.setVisibility(View.GONE);

                FeaturedChallengeSliderView featuredChallengeSliderView = new FeaturedChallengeSliderView(this);
                Challenge tempChallenge = new Challenge(challenge.getTitle(), challenge.getTotalPlayed(), challenge.getChallengeTitleColor(), challenge.getCategoryName(), challenge.getCategoryColor(), challenge.getContentLanguage());
                featuredChallengeSliderView.description(new Gson().toJson(tempChallenge)).
                        image(challenge.getFeatureImage()).setScaleType(BaseSliderView.ScaleType.CenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {
                        if (challenge.getChallengeType().equalsIgnoreCase("article"))
                            ArticleActivity_.intent(context).challenge(challenge).start();
                        else
                            ChallengeDetailActivity_.intent(context).challenge(challenge).start();
                    }
                });
                slider.addSlider(featuredChallengeSliderView);
            } else {
                FeaturedChallengeSliderViewBanner featuredChallengeSliderView = new FeaturedChallengeSliderViewBanner(this);
                featuredChallengeSliderView.image(challenge.getBannerImage()).setScaleType(BaseSliderView.ScaleType.CenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {
                        if (challenge.getBannerFlag() == 1)
                            ReferAndWinActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 2)
                            RedeemPointsActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 3)
                            RafflesActivity_.intent(MainActivity.this).slug(challenge.getSlug()).start();
                        else if (challenge.getBannerFlag() == 4)
                            StoresActivity_.intent(MainActivity.this).slug(challenge.getSlug()).start();
                        else if (challenge.getBannerFlag() == 5)
                            RechargeActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 6)
                            ProfileActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 7)
                            OfflineChallengesActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 8)
                            ChallengeDetailActivity_.intent(MainActivity.this).slug(challenge.getSlug()).start();
                        else if (challenge.getBannerFlag() == 9) {
                            if (spin_flag == 1)
                                SpinWheelActivity_.intent(MainActivity.this).start();
                            else
                                spinWaitingTime();
                        } else if (challenge.getBannerFlag() == 10)
                            ScanActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 12)
                            GaneshaSpeaksActivity_.intent(MainActivity.this).start();
                        else if (challenge.getBannerFlag() == 13) {
                            RaffleContestActivity_.intent(MainActivity.this).start();
                        }
                        else
                            Util.makeToast(MainActivity.this, "Nothing to redirect.Play more challenges and win prizes..!");
                    }
                });
                slider.addSlider(featuredChallengeSliderView);
            }
        }
        if (homeBannerContent.size() > 1) {
            slider.setPresetTransformer(SliderLayout.Transformer.Default);
            slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomIndicator(customIndicator);
            slider.setDuration(4000);
            // slider.addOnPageChangeListener(MainActivity.this);
        } else {
            slider.stopAutoCycle();
            slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            customIndicator.setVisibility(View.GONE);
            customIndicator.destroySelf();
            slider.setEnabled(false);
            slider.setFocusable(false);
        }
    }

    private void setBanner() {
        if (homeBannerContent.size() > 1) {
            initBanner();
        } else if(homeBannerContent.size() == 1){
            single_challenge_main.setVisibility(View.VISIBLE);
            single_challenge.setVisibility(View.VISIBLE);
            //ipl_layout.setVisibility(View.VISIBLE);
            //setStripImage();
            prizesGif.setVisibility(View.VISIBLE);
            if (homeBannerContent.get(0).getTitle() != null) {
                if (homeBannerContent.get(0).getFeatureImage() != null && homeBannerContent.get(0).getFeatureImage().contains("http")) {

                    Picasso.with(context).load(homeBannerContent.get(0).getFeatureImage()).fit().centerCrop().placeholder(R.drawable.default_banner).into(singleImage, new com.squareup.picasso.Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                        }
                    });
                } else {
                    // holder.selector.setVisibility(View.GONE);
                }

                if (homeBannerContent.size() > 0) {
                    singleTitle.setText(homeBannerContent.get(0).getTitle());
                    if (homeBannerContent.get(0).getChallengeTitleColor() != null)
                        singleTitle.setTextColor(Color.parseColor(homeBannerContent.get(0).getChallengeTitleColor()));

                    singlePlays.setText(homeBannerContent.get(0).getTotalPlayed());

                    try {
                        singlePlays.setTextColor(Color.parseColor(homeBannerContent.get(0).getChallengeTitleColor()));
                    }
                    catch (Exception e){
                        singlePlays.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.app_white));
                    };

                    singleLabel.setText(homeBannerContent.get(0).getCategoryName());

                    try{
                        singleLabel.getBackground().setColorFilter(Color.parseColor(homeBannerContent.get(0).getCategoryColor()), PorterDuff.Mode.SRC_IN);
                    }catch (Exception e)
                    {
                        singleLabel.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.app_background_new), PorterDuff.Mode.SRC_IN);
                    }


                    //Log.d("home banner content", new Gson().toJson(homeBannerContent.get(0)));
                    if (homeBannerContent.get(0).getChallengeType().equalsIgnoreCase("article"))
                        overflow_menu.setVisibility(View.GONE);

                   /* if (homeBannerContent.get(0).getOffline() == 1) {
                        singleDownload.setVisibility(View.VISIBLE);
                    } else {
                        singleDownload.setVisibility(View.GONE);
                    }

                    if (homeBannerContent.get(0).getIsDownloaded() == 1) {
                        singleDownload.setImageResource(R.drawable.ic_check_pressed);
                        singleDownload.setClickable(false);
                    } else {
                        singleDownload.setVisibility(View.VISIBLE);
                    }*/
                }
            } else {
                if (homeBannerContent.get(0).getBannerImage() != null && homeBannerContent.get(0).getBannerImage().contains("http")) {

                    Picasso.with(context).load(homeBannerContent.get(0).getBannerImage()).fit().centerCrop().placeholder(R.drawable.default_banner).into(singleImage, new com.squareup.picasso.Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                        }
                    });
                } else {
                    // holder.selector.setVisibility(View.GONE);
                }
                singleTitle.setVisibility(View.GONE);
                singlePlays.setVisibility(View.GONE);
                singleLabel.setVisibility(View.GONE);
                overflow_menu.setVisibility(View.GONE);
                // singleDownload.setVisibility(View.GONE);
            }
        }
    }

    private void setupViewPager() {
        List<String> titles = new ArrayList<>();

        mTabLayout.setVisibility(View.VISIBLE);
        viewpager.setVisibility(View.VISIBLE);

        if (mTabLayout.getChildCount() > 0)
            mTabLayout.removeAllTabs();

        // Log.d("available categories", Util.getCategories(this).toString());

        titles.add(context.getResources().getString(R.string.main_activity_home));
        //titles.add(context.getResources().getString(R.string.main_activity_astrology));
        titles.add(context.getResources().getString(R.string.main_activity_auto));
        titles.add(context.getResources().getString(R.string.main_activity_entertainment));
        titles.add(context.getResources().getString(R.string.main_activity_fashion));
        titles.add(context.getResources().getString(R.string.main_activity_fitness));
        titles.add(context.getResources().getString(R.string.main_activity_food));
        titles.add(context.getResources().getString(R.string.main_activity_f_f));
        titles.add(context.getResources().getString(R.string.main_activity_people));
        titles.add(context.getResources().getString(R.string.main_activity_sports));
        titles.add(context.getResources().getString(R.string.main_activity_tech));
        titles.add(context.getResources().getString(R.string.main_activity_travel));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabsFragments.add(homeFargment());
        //tabsFragments.add(astrologyFragment());
        tabsFragments.add(autoFragment());
        tabsFragments.add(entertainmentFragment());
        tabsFragments.add(fashionFragment());
        tabsFragments.add(fitnessFragment());
        tabsFragments.add(foodFragment());
        tabsFragments.add(natureFragment());
        tabsFragments.add(peopleFragment());
        tabsFragments.add(sportsFragment());
        tabsFragments.add(techFragment());
        tabsFragments.add(travelFragment());

        if (viewpager.getAdapter() == null) {
            FragmentAdapter adapter =
                    new FragmentAdapter(getSupportFragmentManager(), tabsFragments);
            viewpager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(viewpager);
            mTabLayout.setTabsFromPagerAdapter(adapter);
        }
        viewpager.addOnPageChangeListener(this);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setText(titles.get(i));
        }

        if (Util.isFirstOpened(context))
            WelcomeActivity_.intent(this).start();
        else if (Util.isReferOpened(context))
            ReferAndWinActivity_.intent(this).start();

    }

    private HomeFargment homeFargment() {
        return HomeFargment_.builder().build();
    }

    private AstrologyFragment astrologyFragment() {
        return AstrologyFragment_.builder().build();
    }

    private AutoFragment autoFragment() {
        return AutoFragment_.builder().build();
    }

    private EntertainmentFragment entertainmentFragment() {
        return EntertainmentFragment_.builder().build();
    }

    private FashionFragment fashionFragment() {
        return FashionFragment_.builder().build();
    }

    private FitnessFragment fitnessFragment() {
        return FitnessFragment_.builder().build();
    }

    private NatureFragment natureFragment() {
        return NatureFragment_.builder().build();
    }

    private FoodFragment foodFragment() {
        return FoodFragment_.builder().build();
    }

    private PeopleFragment peopleFragment() {
        return PeopleFragment_.builder().build();
    }

    private SportsFragment sportsFragment() {
        return SportsFragment_.builder().build();
    }

    private TechFragment techFragment() {
        return TechFragment_.builder().build();
    }

    private TravelFragment travelFragment() {
        return TravelFragment_.builder().build();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (slider != null) {
            slider.stopAutoCycle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //startIPLCountDown();
        checkConnectivity();

//        hideLoader();
//        isLoaderHidden();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Subscribe
    public void offlinePoints(OfflinePointsEvent offlinePointsEvent) {
        totPoints = String.valueOf(offlinePointsEvent.getCoins());
        pointsNum.setText(String.valueOf(offlinePointsEvent.getCoins()));
        OneSignal.sendTag("Points", String.valueOf(offlinePointsEvent.getCoins()));
    }

    private void startIPLCountDown() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date iplStartDate = dateFormatter.parse("2018-04-07 19:59:59");
            Calendar a = Calendar.getInstance();
            a.setTime(iplStartDate);
            Calendar b = Calendar.getInstance();
            iplCountDown = daysBetween(b, a);
            new CountDownTimer(iplCountDown, 1000) {
                public void onTick(long millisUntilFinished) {
                    String d, h, m, s;
                    d = String.format("%02d", TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                    h = String.format("%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
                    m = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                    s = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    ipl_days.setText(d);
                    ipl_hours.setText(h);
                    ipl_minutes.setText(m);
                    ipl_seconds.setText(s);
                }

                public void onFinish() {

                }
            }.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMillis(Math.abs(end - start));
    }

    private void getWalletDetails() {
        Call<APIResponse<PointsWallet>> callback = apiService.getWalletDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<PointsWallet>>() {
            @Override
            public void onResponse(Response<APIResponse<PointsWallet>> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(MainActivity.this, user);
                                    getWalletDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        //  Points points = new Points();

                                        pointsWallet = (PointsWallet) response.body().getValues();
                                        Util.setPointWallet(MainActivity.this,pointsWallet);
                                        totPoints = String.valueOf(response.body().getValues().getTotal_points());
                                        if (response.body().getValues().getPromotional_total_points() != null)
                                            proPoints = String.valueOf(response.body().getValues().getPromotional_total_points());
                                        else
                                            proPoints = "0";
                                        //   points.setNorPoints(totPoints);
                                        //   points.setSupPoints(proPoints);
                                        //   allPoints.add(points);
                                        spin_flag = response.body().getValues().getSpin_flag();
                                        spin_waiting_time = response.body().getValues().getSpin_waiting_time();
                                        if (response.body().getValues().getSpin_count() != 0) {
                                            spinCount.setVisibility(View.VISIBLE);
                                            spinCount.setText(String.valueOf(response.body().getValues().getSpin_count()));
                                        } else {
                                            spinCount.setVisibility(View.GONE);
                                        }

                                        if (response.body().getValues().getNotification_count() != 0) {
                                            notificationCount.setVisibility(View.VISIBLE);
                                            notificationCount.setText(String.valueOf(response.body().getValues().getNotification_count()));
                                        } else {
                                            notificationCount.setVisibility(View.GONE);
                                        }

                                        OneSignal.sendTag("Points", String.valueOf(response.body().getValues().getTotal_points()));
                                    } else {
                                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
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

    private void getUserAccountDetails() {
        Call<APIResponse<AccountDetails>> callback = apiService.getAccountDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<AccountDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<AccountDetails>> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(MainActivity.this, user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(MainActivity.this, response.body().getValues());
                                        //  pointsNum.setText(String.valueOf(response.body().getValues().getCoinBalance()));
                                        //  menuPoints = String.valueOf(response.body().getValues().getCoinBalance());
                                        //   OneSignal.sendTag("Points", String.valueOf(response.body().getValues().getCoinBalance()));
                                        superPoints = String.valueOf(response.body().getValues().getPromCoins());
                                        if (response.body().getValues().getFirstOpened() == 1)
                                            Util.setFirstOpened(MainActivity.this, "true");
                                        else
                                            Util.setFirstOpened(MainActivity.this, "");

                                        if (response.body().getValues().getReferOpened() == 1)
                                            Util.setReferOpened(MainActivity.this, "true");
                                        else
                                            Util.setReferOpened(MainActivity.this, "");

                                        if (response.body().getValues().getPromoFlag() == 1)
                                            Util.setSuperPointsOpened(MainActivity.this, "true");
                                        else
                                            Util.setSuperPointsOpened(MainActivity.this, "");
                                    } else {
                                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
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

    @Subscribe
    public void operationalCityId(CityUpdateEvent cityIdEvent) {
        finish();
        JSONObject jo = new JSONObject();
        Util.saveSelectedCity(this, "");
        Util.saveSelectedCityId(this, "");
        Util.saveSelectedCity(this, cityIdEvent.getCityName());
        Util.saveSelectedCityId(this, cityIdEvent.getCityId());
        try {
            if (cityIdEvent.getCityId() != null) {
                jo.put("cityId", cityIdEvent.getCityId());
                MainActivity_.intent(this).serachLocatioReq(jo.toString()).start();
            } else {
                jo.put("latLng", cityIdEvent.getCityLatLng());
                MainActivity_.intent(this).serachLocatioReq(jo.toString()).start();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void userCurrentLocation(LocationEvent locationEvent) {
        finish();
        JSONObject jo = new JSONObject();
        try {
            if (locationEvent.getLocation() != null) {
                String myLocation = locationEvent.getLocation().getLatitude() + "," + locationEvent.getLocation().getLongitude();
                jo.put("latLng", myLocation);
                MainActivity_.intent(this).serachLocatioReq(jo.toString()).start();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllChallengesWithTabs(JSONObject data) {
        hideLoader();
        showLoader();

        Call<APIResponse<List<HomeCategory>>> callback = null;
        try {
            if (data.has("latLng")) {
                String[] coords = data.getString("latLng").split(",");
                callback = apiService.getChallengeWithTabs(Constants.MB_API_KEY, user.getAuthToken(), coords[0], coords[1], Util.getDeviceDPI(this));
            } else if (data.has("cityId")) {
                callback = apiService.getChallengeWithTabs(Constants.MB_API_KEY, user.getAuthToken(), data.getString("cityId"), Util.getDeviceDPI(this));
            } else {
                callback = apiService.getChallengeWithTabs(Constants.MB_API_KEY, user.getAuthToken(), Util.getDeviceDPI(this));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        callback.enqueue(new Callback<APIResponse<List<HomeCategory>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<HomeCategory>>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().getStatusCode() == 401) {
                        if (response.body().getMeta().isStatus()) {
                            user.setAuthToken(response.body().getMeta().getNewAuthToken());
                            Util.setUser(MainActivity.this, user);
                            getAllChallengesWithTabs(new JSONObject());
                        } else {
                            oopsLogout(response.body().getMeta().getMessage());
                        }
                    } else {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                homeCategories = response.body().getValues();
                                //Log.d("tabs response", new Gson().toJson(homeCategories));
                                // loadDrawer();
                                // setupViewPager();
                            }
                        }
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
                Log.d("tab list fail message", t.getMessage());
            }
        });
    }

    private void getContacts() {
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (pCur.moveToNext()) {
            String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(phoneNumber);
        }
        pCur.close();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*if (position == 0) {
            carousel.setVisibility(View.VISIBLE);
        } else {
            carousel.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void saveChallengeOffline() {
        if (homeBannerContent.get(0).getOffline() == 1) {
            // singleDownProgress.setVisibility(View.VISIBLE);
            // singleDownload.setVisibility(View.GONE);
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_downloading) + " " + homeBannerContent.get(0).getTitle() + " " + context.getResources().getString(R.string.challenge_adapter_challenge));
            Call<APIResponse<Challenge>> callback = apiService.saveChallengeOffline(Constants.MB_API_KEY, user.getAuthToken(), homeBannerContent.get(0).getSlug());
            callback.enqueue(new retrofit.Callback<APIResponse<Challenge>>() {
                @Override
                public void onResponse(Response<APIResponse<Challenge>> response) {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(MainActivity.this, user);
                                    saveChallengeOffline();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        runDownloadTask(response.body().getValues());
                                        DbHelper.getInstance(MainActivity.this).saveChallenge(response.body().getValues(), "offline");
                                    } else {
                                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                                    // singleDownProgress.setVisibility(View.GONE);
                                    // singleDownload.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    //  singleDownProgress.setVisibility(View.GONE);
                    //  singleDownload.setVisibility(View.VISIBLE);
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            });
        } else {
            Util.makeToast(MainActivity.this, context.getResources().getString(R.string.challenge_adapter_cannot_play_offline));
        }
    }

    private void runDownloadTask(Challenge challenge) {
        downloadURLs = new ArrayList<>();
        for (int i = 0; i < challenge.getSteps().size(); i++) {
            Step s = challenge.getSteps().get(i);
            if (s.getUrl() != null &&
                    !s.getUrl().equalsIgnoreCase("") &&
                    s.getUrl().toLowerCase().contains("http://") &&
                    !s.getUrl().toLowerCase().contains("youtube.com")) {
                downloadURLs.add(s.getUrl());
                //Log.d("Downloadable URL", s.getUrl());
            } else if (s.getImage() != null && s.getImage().toLowerCase().contains("http://")) {
                downloadURLs.add(s.getImage());
            }
        }

        //image mapping
        for (int i = 0; i < challenge.getSteps().size(); i++) {
            Step s = challenge.getSteps().get(i);
            if (s.getType().contentEquals("Image Mapping")) {
                for (int j = 0; j < s.getImages().size(); j++) {
                    Memimages images = s.getImages().get(j);
                    if (images.getUrl() != null &&
                            !images.getUrl().equalsIgnoreCase("") &&
                            images.getUrl().toLowerCase().contains("https://")) {
                        downloadURLs.add(images.getUrl());
                        //Log.d("im map Downloadable URL", images.getUrl());
                    }
                }
            }
        }
        //image mapping

        if (challenge.getBackgroundImage() != null && challenge.getBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getBackgroundImage());
            //Log.d("back Downloadable URL", challenge.getBackgroundImage());
        }

        if (challenge.getStartBackgroundImage() != null && challenge.getStartBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getStartBackgroundImage());
            //Log.d("start Downloadable URL", challenge.getStartBackgroundImage());
        }

        if (challenge.getEndBackgroundImage() != null && challenge.getEndBackgroundImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getEndBackgroundImage());
            //Log.d("end Downloadable URL", challenge.getEndBackgroundImage());
        }

        if (challenge.getCoverImage() != null && challenge.getCoverImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getCoverImage());
            //Log.d("cover Downloadable URL", challenge.getCoverImage());
        }

        if (challenge.getDealImage() != null && challenge.getDealImage().toLowerCase().contains("http")) {
            downloadURLs.add(challenge.getDealImage());
            //Log.d("deal Downloadable URL", challenge.getDealImage());
        }

        if (downloadURLs.size() > 0) {
            multiParallelDownload(challenge.getSlug());
        } else {
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_download_completed));
            //Log.d("Download", "Challenge Downloaded");
            //  singleDownProgress.setVisibility(View.GONE);
            //  singleDownload.setVisibility(View.VISIBLE);
            //  singleDownload.setImageResource(R.drawable.ic_check_pressed);
            //  singleDownload.setClickable(false);
            PopupMenu popup = new PopupMenu(context, overflow_menu);
            popup.inflate(R.menu.options_menu);
            MenuItem item = popup.getMenu().findItem(R.id.download);
            item.setTitle(context.getResources().getString(R.string.challenge_adapter_downloaded));
            homeBannerContent.get(0).setIsDownloaded(1);
            //Log.d("DownloadTask", "NoDownloads");
        }
    }

    public void multiParallelDownload(String challengeSlug) {
        final FileDownloadListener parallelTarget = createListener();
        int i = 0;
        for (String url : downloadURLs) {
            totalCounts++;
            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
            FileDownloader.getImpl().create(url)
                    .setListener(parallelTarget)
                    .setPath(Constants.FOLDER_PATH(challengeSlug, fileName))
                    .setCallbackProgressTimes(1)
                    .setTag(++i)
                    .ready();
        }

        FileDownloader.getImpl().start(parallelTarget, false);
    }

    private FileDownloadListener createListener() {
        return new FileDownloadListener() {

            @Override
            public boolean callback(IDownloadEvent event) {
                if (isFinishing()) {
                    return false;
                }
                return super.callback(event);
            }

            @Override
            protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {

            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);

            }

            @Override
            protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                //Log.d("DownloadTask", finalCounts + " - " + soFarBytes + " - " + totalBytes);
            }

            @Override
            protected void blockComplete(final BaseDownloadTask task) {
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                //Log.d("Downloaded Path", task.getPath());
                resetCounters();
            }

            @Override
            protected void paused(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                resetCounters();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                resetCounters();
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                resetCounters();
            }
        };
    }

    private void resetCounters() {
        finalCounts++;
        if (finalCounts == totalCounts) {
            //  singleDownProgress.setVisibility(View.GONE);
            //  singleDownload.setVisibility(View.VISIBLE);
            //  singleDownload.setImageResource(R.drawable.ic_check_pressed);
            //  singleDownload.setClickable(false);
            PopupMenu popup = new PopupMenu(context, overflow_menu);
            popup.inflate(R.menu.options_menu);
            MenuItem item = popup.getMenu().findItem(R.id.download);
            item.setTitle(context.getResources().getString(R.string.challenge_adapter_downloaded));
            homeBannerContent.get(0).setIsDownloaded(1);
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_download_completed));
            //Log.d("DownloadTask", "Finished");
            finalCounts = 0;
            totalCounts = 0;
            downloadURLs = new ArrayList<>();
        }
    }

    private void setFontToMenu() {
        MenuItem menuItem1 = toolbar.getMenu().findItem(R.id.my_account);
        applyFontToMenuItem(menuItem1);
        MenuItem menuItem2 = toolbar.getMenu().findItem(R.id.redeem);
        applyFontToMenuItem(menuItem2);
        MenuItem menuItem3 = toolbar.getMenu().findItem(R.id.offline_chall);
        applyFontToMenuItem(menuItem3);
        MenuItem menuItem4 = toolbar.getMenu().findItem(R.id.settings);
        applyFontToMenuItem(menuItem4);
        MenuItem menuItem5 = toolbar.getMenu().findItem(R.id.qr_code);
        applyFontToMenuItem(menuItem5);
        MenuItem menuItem6 = toolbar.getMenu().findItem(R.id.winners);
        applyFontToMenuItem(menuItem6);
        MenuItem menuItem7 = toolbar.getMenu().findItem(R.id.logout);
        applyFontToMenuItem(menuItem7);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        // Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat_Regular.ttf");
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        SpannableStringBuilder mNewTitle = new SpannableStringBuilder(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void getDownloadedChallenges() {
        user = Util.getUser(this);
        Call<APIResponse<List<DownloadedChallenges>>> callback = apiService.getDownChallenges(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<DownloadedChallenges>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<DownloadedChallenges>>> response) {

                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            Util.saveDownChalSlugs(MainActivity.this, response.body().getValues());
                            Log.d("downchal response", new Gson().toJson(response.body().getValues()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void spinWaitingTime() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.spin_waiting_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView spin_time = (TextView) promptView.findViewById(R.id.spin_time);
        Button spin_close = (Button) promptView.findViewById(R.id.spin_wait_close);
        long spin_wait_time = spin_waiting_time * 1000;
        new CountDownTimer(spin_wait_time, 1000) {
            public void onTick(long millisUntilFinished) {
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                spin_time.setText(hms);
            }

            public void onFinish() {
                spin_time.setText(R.string.main_activity_spin_now);
            }
        }.start();
        spin_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void appUpdate() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.app_update_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView appUpdateTitle = (TextView) promptView.findViewById(R.id.dialog_title);
        Button appUpdate = (Button) promptView.findViewById(R.id.dialog_action_btn);
        ImageView gifView = (ImageView) promptView.findViewById(R.id.imageViewOverlay);
        Glide.with(this).load(R.raw.app_update).into(gifView);
        appUpdateTitle.setTypeface(font);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        appUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.intentRateUs((Activity) MainActivity.this);
                finish();
            }
        });
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public void onFloatingActionClicked(View view) {

//        Util.resetIPLStarted(this);

//        String ownTeam = Util.getOwnTeamSelected(this);
//        if (ownTeam.equalsIgnoreCase("")) {
//            CricketOwnYourTeamActivity_.intent(MainActivity.this).start();
//        } else {
//            CricketHomeActivity_.intent(MainActivity.this).start();
//        }

//        CricketStandingsActivity_.intent(MainActivity.this).start();
//        CricketFixturesActivity_.intent(MainActivity.this).start();
//        CricketOwnYourTeamActivity_.intent(MainActivity.this).start();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //checkConnectivity();
    }

    private void profileUpdateDialog(int profilePercentage, int genderFlag, int dobFlag, int locationFlag){
        dismissDialog();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.profile_update_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        profileTitle = (TextView) promptView.findViewById(R.id.profile_update_title);
        TextView maleText = (TextView) promptView.findViewById(R.id.male_text);
        TextView femaleText = (TextView) promptView.findViewById(R.id.female_text);
        LinearLayout genderSelection = (LinearLayout) promptView.findViewById(R.id.gender_selection);
        LinearLayout dob = (LinearLayout) promptView.findViewById(R.id.dob);
        dobPickerHldr = (LinearLayout) promptView.findViewById(R.id.dob_hldr);
        dobDatepicker = (DatePicker) promptView.findViewById(R.id.dob_datepicker);
        LinearLayout linearList = (LinearLayout) promptView.findViewById(R.id.linear_list);
        LinearLayout location = (LinearLayout) promptView.findViewById(R.id.location);
        Button profileUpdate = (Button) promptView.findViewById(R.id.profile_update);
        profileUpdateDesc = (TextView) promptView.findViewById(R.id.profile_update_desc);
        ImageView maleImage = (ImageView) promptView.findViewById(R.id.male_image);
        ImageView femaleImage = (ImageView) promptView.findViewById(R.id.female_image);
        ProgressBar profileProgress = (ProgressBar) promptView.findViewById(R.id.profile_progress);
        TextView progressPercentage = (TextView) promptView.findViewById(R.id.progress_percentage);
        TextView or = (TextView) promptView.findViewById(R.id.or);
        TextView skip = (TextView) promptView.findViewById(R.id.skip);
        EditText date = (EditText) promptView.findViewById(R.id.date);
        EditText month = (EditText) promptView.findViewById(R.id.month);
        EditText year = (EditText) promptView.findViewById(R.id.year);
        EditText userLocation = (EditText) promptView.findViewById(R.id.user_location);
        ListView locationList = (ListView) promptView.findViewById(R.id.list_views);
        profileTitle.setTypeface(font);
        maleText.setTypeface(font);
        femaleText.setTypeface(font);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        profileUpdate(profilePercentage, genderFlag, dobFlag, locationFlag , genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);


        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add( Calendar.YEAR, -5 ); // set 5 years as a minimum age limit of child
        long minDate = c.getTime().getTime();

        dobDatepicker.setMaxDate(minDate);


        alertDialog = builder.create();
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        SharedPreferences preferences = getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(AppSettings.PREF_IS_UPDATE_PROFILE_DIALOG_VISIBLE, true).commit();

    }

    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void profileUpdate(int profilePercentage, int genderFlag, int dobFlag, int locationFlag, LinearLayout genderSelection, LinearLayout dob, LinearLayout linearList, LinearLayout location, Button profileUpdate, ImageView maleImage, ImageView femaleImage, ProgressBar profileProgress, TextView progressPercentage, TextView or, TextView skip, EditText date, EditText month, EditText year, EditText userLocation, ListView locationList) {
        if (profilePercentage != 100) {
            googleAPIService = GoogleAPIClient.getAPIService();
            if (genderFlag == 1) {
                genderSelection.setVisibility(View.VISIBLE);
              //  dob.setVisibility(View.GONE);
                dobPickerHldr.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                profileUpdate.setText("Next");
                profileTitle.setText(getResources().getString(R.string.title_for_update_gender));

            } else if (dobFlag == 1) {
                genderSelection.setVisibility(View.GONE);
              //  dob.setVisibility(View.VISIBLE);
                dobPickerHldr.setVisibility(View.VISIBLE);
                location.setVisibility(View.GONE);
                profileUpdate.setText("Next");
                profileTitle.setText(getResources().getString(R.string.title_for_update_dob));
            } else if (locationFlag == 1) {
                genderSelection.setVisibility(View.GONE);
               // dob.setVisibility(View.GONE);
                dobPickerHldr.setVisibility(View.GONE);
                location.setVisibility(View.VISIBLE);
                profileUpdate.setText("Update");
                profileTitle.setText(getResources().getString(R.string.title_for_update_location));
            }
            profileProgress.setMax(100);
            //profileProgress.setProgress(50);
            progressPercentage.setText(profilePercentage + "" + "%");
            ObjectAnimator animation = ObjectAnimator.ofInt(profileProgress, "progress", profilePercentage);
            animation.setDuration(500); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            profileUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(genderSelection.getVisibility() == View.VISIBLE){
                        if (!gender.equalsIgnoreCase("")) {
                            //postProfileDetails(gender, "", "", genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);

                            postProfileDetails(gender, "", "", genderSelection, dobPickerHldr, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);

                        }else {
                            Util.makeToast(MainActivity.this, "Please select gender");
                        }
                    } else if (dobPickerHldr.getVisibility() == View.VISIBLE){
                       // if (!date.getText().toString().isEmpty() && !month.getText().toString().isEmpty() && !year.getText().toString().isEmpty()) {
                        if (!String.valueOf(dobDatepicker.getDayOfMonth()).isEmpty() && !String.valueOf(dobDatepicker.getMonth()).isEmpty() && !String.valueOf(dobDatepicker.getYear()).isEmpty()) {
                           // String userDoB = date.getText().toString() + "/" + month.getText().toString() + "/" + year.getText().toString();
                            //String userDoB = dobDatepicker.getDayOfMonth() + "/" + dobDatepicker.getMonth() +"/" + dobDatepicker.getYear();

                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                            int dd = dobDatepicker.getDayOfMonth();
                            int mm = dobDatepicker.getMonth();
                            int yy = dobDatepicker.getYear();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(yy,mm,dd);
                            String userDoB = format.format(calendar.getTime());

                         //   postProfileDetails("", userDoB, "", genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);

                            postProfileDetails("", userDoB, "", genderSelection, dobPickerHldr, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);

                        }else {
                            Util.makeToast(MainActivity.this, "Please enter date of birth");
                        }
                    } else if (location.getVisibility() == View.VISIBLE){
                        if (!userLocation.getText().toString().trim().isEmpty()) {
                          //  postProfileDetails("", "", userLocation.getText().toString(), genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);
                            postProfileDetails("", "", userLocation.getText().toString(), genderSelection, dobPickerHldr, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);


                        }else {
                            Util.makeToast(MainActivity.this, "Please enter location");
                        }
                    }
                }
            });
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            maleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    maleImage.setSelected(true);
                    femaleImage.setSelected(false);
                    gender = "male";
                }
            });
            femaleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    femaleImage.setSelected(true);
                    maleImage.setSelected(false);
                    gender = "female";
                }
            });
            date.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dob = date.getText().toString();
                    if (dob.length() == 2) {
                        month.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            month.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String dob = month.getText().toString();
                    if (dob.length() == 2) {
                        year.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            userLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int count) {
                    if (s.length() > 0) {
                   /* locationList.setVisibility(View.VISIBLE);
                    profileUpdate.setVisibility(View.GONE);
                    or.setVisibility(View.GONE);
                    skip.setVisibility(View.GONE);*/
                        getGooglePlaces(s.toString(), profileUpdate, or, skip, linearList, locationList);
                    } else {
                        linearList.setVisibility(View.GONE);
                        profileUpdate.setVisibility(View.VISIBLE);
                        profileUpdateDesc.setVisibility(View.VISIBLE);
                        or.setVisibility(View.VISIBLE);
                        skip.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    GooglePrediction item = (GooglePrediction) adapterView.getAdapter().getItem(position);
                    userLocation.setText(item.getDescription());
                    linearList.setVisibility(View.GONE);
                    profileUpdate.setVisibility(View.VISIBLE);
                    profileUpdateDesc.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.VISIBLE);
                }
            });
        } else {
            dismissDialog();
        }
    }

    public void getGooglePlaces(String text, Button update, TextView or, TextView skip, LinearLayout linearList, ListView locationList) {
        Call<GoogleAPIResponse<List<GooglePrediction>>> callBack = null;
        try {
            callBack = googleAPIService.getGooglePlaces("false", "en", Constants.GOOGLE_APP_API_BROWSER_KEY, URLEncoder.encode(text, "utf8"), "country:in");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callBack.enqueue(new Callback<GoogleAPIResponse<List<GooglePrediction>>>() {
            @Override
            public void onResponse(Response<GoogleAPIResponse<List<GooglePrediction>>> response) {
                Log.d("Retrofit Response", new Gson().toJson(response.body()));
                List<GooglePrediction> predictions = new ArrayList<GooglePrediction>();

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        predictions = response.body().getPredictions();
                        drawSearchedLocationList(predictions, update, or, skip, linearList, locationList);
                    } else {
                        //Util.makeToast(MainActivity.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                    }
                } else {
                    Util.makeToast(MainActivity.this, "Not able to fetch location at this points of time.Please try again or enter manually");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null)
                    Log.d("Retrofit Response", new Gson().toJson(t));
            }
        });
    }

    private void drawSearchedLocationList(List<GooglePrediction> values, Button profileUpdate, TextView or, TextView skip, LinearLayout linearList, ListView loc) {
        linearList.setVisibility(View.VISIBLE);
        profileUpdate.setVisibility(View.GONE);
        profileUpdateDesc.setVisibility(View.GONE);
        or.setVisibility(View.GONE);
        skip.setVisibility(View.GONE);
        projectItemsAdapter = new CustomListAdapter<>(values, this::searchedLocationItem);
        loc.setAdapter(projectItemsAdapter);
    }

    private SearchedLocationItem searchedLocationItem() {
        SearchedLocationItem item = SearchedLocationItem_.build(this);
        return item;
    }

    public void getProfileDetails() {
        Call<APIResponse<ProfileDetails>> callback = apiService.getProfileDetails(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<ProfileDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<ProfileDetails>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta().statusCode == 200) {
                        int profilePertcentage = response.body().getValues().getProfilePercentage();
                        int genderFlag = response.body().getValues().getGenderFlag();
                        int dobFlag = response.body().getValues().getDobFlag();
                        int locationFlag = response.body().getValues().getLocationFlag();
                        if (genderFlag == 1 || dobFlag == 1 || locationFlag == 1)
                            profileUpdateDialog(profilePertcentage, genderFlag, dobFlag, locationFlag);
                    } else {
                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    public void postProfileDetails(String updatedGender, String updatedDob, String updatedLocation, LinearLayout genderSelection, LinearLayout dob, LinearLayout linearList, LinearLayout location, Button profileUpdate, ImageView maleImage, ImageView femaleImage, ProgressBar profileProgress, TextView progressPercentage, TextView or, TextView skip, EditText date, EditText month, EditText year, EditText userLocation, ListView locationList) {
        Call<APIResponse<ProfileDetails>> callback = apiService.postProfileDetails(Constants.MB_API_KEY, user.getAuthToken(), updatedGender, updatedDob, updatedLocation);
        callback.enqueue(new Callback<APIResponse<ProfileDetails>>() {
            @Override
            public void onResponse(Response<APIResponse<ProfileDetails>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta().statusCode == 200) {
                        int profilePertcentage = response.body().getValues().getProfilePercentage();
                        int genderFlag = response.body().getValues().getGenderFlag();
                        int dobFlag = response.body().getValues().getDobFlag();
                        int locationFlag = response.body().getValues().getLocationFlag();

                        if(updatedGender != null && !updatedGender.trim().isEmpty())
                            Util.getUser(MainActivity.this).setGender(updatedGender);

                        if(updatedDob != null && !updatedDob.trim().isEmpty())
                            Util.getUser(MainActivity.this).setDob(updatedDob);

                        if(updatedLocation != null && !updatedLocation.trim().isEmpty())
                            Util.getUser(MainActivity.this).setLocation(updatedLocation);

                        getWalletDetails();

                        profileUpdate(profilePertcentage, genderFlag, dobFlag, locationFlag, genderSelection, dob, linearList, location, profileUpdate, maleImage, femaleImage, profileProgress, progressPercentage, or, skip, date, month, year, userLocation, locationList);
                    } else {
                        Util.makeToast(MainActivity.this, response.body().getMeta().getMessage());
                    }
                } else {
                    Util.makeToast(MainActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //revokeDeepStreamRecordForChallengeContest();
     //   getApp().closeDeepStream();
    }
}






