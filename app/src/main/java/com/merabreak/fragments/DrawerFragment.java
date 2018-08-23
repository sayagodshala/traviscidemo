package com.merabreak.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.merabreak.BaseFragment;
import com.merabreak.BuildConfig;
import com.merabreak.BusProvider;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.LocationClient;
import com.merabreak.LocationEvent;
import com.merabreak.MainActivity;
import com.merabreak.NavigationItemType;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.AboutUsActivity_;
import com.merabreak.activities.ChallengesActivity_;
import com.merabreak.activities.FAQActivity_;
import com.merabreak.activities.OfflineChallengesActivity_;
import com.merabreak.activities.OneTimeChallengeTCActivity_;
import com.merabreak.activities.PrivacyPolicyActivity_;
import com.merabreak.activities.ProfileActivity_;
import com.merabreak.activities.RechargeActivity_;
import com.merabreak.activities.ReferAndWinActivity_;
import com.merabreak.activities.RegistrationActivityNew_;
import com.merabreak.activities.ScanActivity_;
import com.merabreak.activities.SearchLocationActivity_;
import com.merabreak.activities.SpinWheelActivity_;
import com.merabreak.activities.TermsAndConditionsActivity_;
import com.merabreak.controls.ProjectDrawerNavigationItem;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.challenge.Category;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;
import rx.subjects.PublishSubject;


@EFragment(R.layout.drawer)
public class DrawerFragment extends BaseFragment /*implements LocationClient.LocationClientListener*/ {
    private PublishSubject<NavigationItemType> navigationItemSelection = PublishSubject.create();

    ProjectDrawerNavigationItem lastSelectedDrawerItem, lastSelectedDrawerHeaderItem;

    private ImageLoader mImageLoader;

    @ViewById(R.id.user_name)
    TextView userName;

    @ViewById(R.id.user_rank)
    TextView userRank;

    @ViewById(R.id.points)
    TextView points;

    @ViewById(R.id.user_address)
    TextView userAddress;

    @ViewById(R.id.post_login)
    RelativeLayout postLogin;

    @ViewById(R.id.before_login)
    RelativeLayout beforeLogin;

    @ViewById(R.id.category)
    RelativeLayout category;

    @ViewById(R.id.category_menus)
    LinearLayout categoryMenus;

    @ViewById(R.id.category_arrow)
    View categoryArrow;

    @ViewById(R.id.app_ver_code)
    TextView appVersionName;

    @ViewById(R.id.profile_image)
    ImageView profileImage;
    private List<Category> items = new ArrayList<>();
    private int selectedPos = -1;

    private LocationClient locationClient;
    private Location myLocation = null;

    @ViewById(R.id.address_cont)
    RelativeLayout addressCont;

    @AfterViews
    void init() {
        getUserAccountDetails();

        FontUtils.setDefaultFont(getActivity(), getActivity().findViewById(android.R.id.content));

        String latlng = Util.getLatLng(getActivity());
        if (!latlng.equalsIgnoreCase("")) {
            String[] coords = latlng.split(",");
            if (coords.length > 0) {
                //Log.d("LatLng", "Already Saved");
                address(new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
            }
        }

        String appVersion = String.valueOf(BuildConfig.VERSION_NAME);
        appVersionName.setText("App Version" + " : " + appVersion);
        BusProvider.bus().register(getActivity());
        getCategories();
        categoryMenus.setVisibility(View.GONE);
        categoryArrow.setSelected(false);
        String cityName = Util.getSelectedCity(getActivity());
        if (latlng.equalsIgnoreCase("")) {
            if (!cityName.equalsIgnoreCase("")) {
                userAddress.setText(cityName);
            }
        }
    }

    @Click(R.id.post_login)
    void openUserProfile() {
        ProfileActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.challenges)
    void onChallenges() {
        ChallengesActivity_.intent(getActivity()).filterType("New").start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.offline_challenges)
    void onOfflineChallenges() {
        OfflineChallengesActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.scan_qr)
    void scanQRCode() {
        ScanActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.refer)
    void referAndWin() {
        ReferAndWinActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.rate)
    void rateUs() {
        Util.intentRateUs(getActivity());
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.daily_spin)
    void dailySpinWheel() {
        SpinWheelActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.about)
    void aboutUs() {
        AboutUsActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.one_day_cha_t_and_c)
    void oneTimeTermsConditions() {
        OneTimeChallengeTCActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.terms)
    void termsConditions() {
        TermsAndConditionsActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.privacy)
    void privacyPolicy() {
        PrivacyPolicyActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.faq)
    void showFAQ() {
        FAQActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.app_version)
    void appVersionCode() {
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.mobile_recharge)
    void onRecharge() {
        if (Util.isUserLoggedIn(getActivity())) {
            RechargeActivity_.intent(getActivity()).start();
            ((MainActivity) getActivity()).closeDrawer();
        } else {
            Util.makeToast(getActivity(), "Please Signup To Access Recharge Option");
            ((MainActivity) getActivity()).closeDrawer();
        }
    }

    @Click(R.id.before_login)
    void openRegistration() {
        RegistrationActivityNew_.intent(getActivity()).mobile(user.getPhone()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    @Click(R.id.signup_button)
    void openRegButt() {
        RegistrationActivityNew_.intent(getActivity()).mobile(user.getPhone()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    private void navigationHeaderSelected(ProjectDrawerNavigationItem drawerItemView, View childContainer) {
        toggleVisibility(childContainer);
//        trackDrawerItemHeaderSelection(drawerItemView);
    }

    private void navigationItemSelected(ProjectDrawerNavigationItem drawerItemView) {
        trackDrawerItemSelection(drawerItemView);
        navigationItemSelection.onNext(drawerItemView.getNavigationItem().getNavigationItemType());
    }

    private void trackDrawerItemSelection(ProjectDrawerNavigationItem drawerItemView) {
        if (lastSelectedDrawerItem != null)
            lastSelectedDrawerItem.getItem().setSelected(false);
        lastSelectedDrawerItem = drawerItemView;
        drawerItemView.getItem().setSelected(true);
    }

    private void trackDrawerItemHeaderSelection(ProjectDrawerNavigationItem drawerHeaderItemView) {
        if (lastSelectedDrawerHeaderItem != null)
            lastSelectedDrawerHeaderItem.getItem().setSelected(false);
        lastSelectedDrawerHeaderItem = drawerHeaderItemView;
        drawerHeaderItemView.getItem().setSelected(true);
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }

    private LayoutParams leafNodeLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(navStartMargin(), 0, 0, 0);
        return layoutParams;
    }

    private int navStartMargin() {
        return context.getResources().getDimensionPixelSize(R.dimen.nav_item_start_padding);
    }

    public Observable<NavigationItemType> navigationItemSelected() {
        return bindObservable(navigationItemSelection.asObservable());
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void bindData() {
        if (user != null && user.getEmail() != null) {
            beforeLogin.setVisibility(View.GONE);
            postLogin.setVisibility(View.VISIBLE);
            userName.setText(user.getFullName());
            try {
                URI uri = new URI(user.getImage());
                if (uri.getHost() != null) {
                    Picasso.with(context).load(user.getImage()).placeholder(R.drawable.dummy_profile_image).into(profileImage, new com.squareup.picasso.Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                        }
                    });
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            AccountDetails accountDetails = Util.getUserAccountDetails(getActivity());
            if (accountDetails != null) {
                points.setText(accountDetails.getCoinBalance() + " " + context.getResources().getString(R.string.points));
                userRank.setText(accountDetails.getCurrentRank());
                if (accountDetails.getCurrentRank() != null) {
                    userRank.setVisibility(View.VISIBLE);
                }
            }
        } else {
            beforeLogin.setVisibility(View.VISIBLE);
            postLogin.setVisibility(View.GONE);
        }
        FontUtils.setDefaultFont(getActivity(), userName);
        FontUtils.setDefaultFont(getActivity(), points);
        FontUtils.setDefaultFont(getActivity(), userRank);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.bus().unregister(getActivity());
        if (locationClient != null) {
            locationClient.disconnect();
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
                                    Util.setUser(getActivity(), user);
                                    getUserAccountDetails();
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        Util.saveUserAccountDetails(getActivity(), response.body().getValues());
                                        points.setText(response.body().getValues().getCoinBalance() + " " + context.getResources().getString(R.string.points));
                                        userRank.setText(response.body().getValues().getCurrentRank());
                                       // Log.d("drawer frag user det", new Gson().toJson(response.body().getValues()));
                                    } else {
                                        Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                                    }
                                } else {
                                    Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
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

    private void drawMenus() {

        if (categoryMenus.getChildCount() > 0)
            categoryMenus.removeAllViews();

        items = Util.getCategories(getActivity());
        for (int i = 0; i < items.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_category, null);
            Button check = (Button) view.findViewById(R.id.check);
            check.setTag(i + "");
            check.setText(items.get(i).getTitle());
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        int pos = Integer.parseInt(v.getTag().toString());

                        ((MainActivity) getActivity()).closeDrawer();
                        ChallengesActivity_.intent(getActivity()).category(items.get(pos)).start();
                    }
                }
            });
            categoryMenus.addView(view);
            FontUtils.setDefaultFont(getActivity(), categoryMenus);
        }

        if (items.size() > 0)
            category.setVisibility(View.VISIBLE);
        else
            category.setVisibility(View.GONE);

    }

    private void deselectAnswers() {
        for (int i = 0; i < categoryMenus.getChildCount(); i++) {
            View answerView = categoryMenus.getChildAt(i);
            Button check = (Button) answerView.findViewById(R.id.check);
            check.setSelected(false);
        }
    }

    @Click(R.id.category)
    void onCategoryClick() {
        if (categoryMenus.getVisibility() == View.VISIBLE) {
            categoryMenus.setVisibility(View.GONE);
            categoryArrow.setSelected(false);
        } else {
            categoryMenus.setVisibility(View.VISIBLE);
            categoryArrow.setSelected(true);
        }
    }

    @Click(R.id.address_cont)
    void onAddress() {
        SearchLocationActivity_.intent(getActivity()).start();
        ((MainActivity) getActivity()).closeDrawer();
    }

    private void getCategories() {
        Call<APIResponse<List<Category>>> callback = apiService.getCategories(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Category>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Category>>> response) {
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(getActivity(), user);
                                getCategories();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                                if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                                    Util.saveCategories(getActivity(), response.body().getValues());
                                    drawMenus();
                                }else{
                                    Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                                }
                            }else{
                                Util.makeToast(getActivity(), response.body().getMeta().getMessage());
                            }
                        }
                    }else{
                        Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                    }
                }else{
                    Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                }

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Background
    void address(LatLng location) {
        if (getActivity() == null)
            return;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
//                setAddress(new String[]{addresses.get(0).getAddressLine(0), addresses.get(0).getAddressLine(1)});
//                setAddress(address.getLocality());
                setAddress(addresses);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setAddress(String[] addresss) {
        StringBuilder bindAddress = new StringBuilder();
        for (String value : addresss) {
            if (bindAddress.toString().equalsIgnoreCase("")) {
                bindAddress.append(value);
            } else {
                bindAddress.append(", " + value);
            }
        }

        if (!bindAddress.toString().equalsIgnoreCase("")) {
            userAddress.setText(bindAddress.toString());
        }
    }

    @UiThread
    void setAddress(String addresss) {
        userAddress.setText(addresss);
    }

    @UiThread
    void setAddress(List<Address> addresses) {
        Address address = addresses.get(0);
        //String[] addresss = new String[]{addresses.get(0).getAddressLine(0), addresses.get(0).getAddressLine(1)}; 03-jan
        String[] addresss = new String[]{addresses.get(0).getAddressLine(1)};
        StringBuilder bindAddress = new StringBuilder();

       /* if (address.getSubLocality() != null && !address.getSubLocality().equalsIgnoreCase("")) {   03-jan
            bindAddress.append(address.getSubLocality());
        }*/

        if (address.getLocality() != null && !address.getLocality().equalsIgnoreCase("")) {
            if (!bindAddress.toString().equalsIgnoreCase("")) {
                //bindAddress.append(", " + address.getLocality());  03-jan
                bindAddress.append(address.getLocality());
            } else {
                bindAddress.append(address.getLocality());
            }
        }

        if (bindAddress.toString().equalsIgnoreCase("")) {
            for (String value : addresss) {
                if (bindAddress.toString().equalsIgnoreCase("")) {
                    bindAddress.append(value);
                } else {
                    //bindAddress.append(", " + value);  03-jan
                    bindAddress.append(value);
                }
            }
        }
        userAddress.setText(bindAddress.toString());
        //addressCont.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        if (event.getLocation() != null) {
            //Log.d("LocationEvent", "Fired");
            address(new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
        }
    }

    /*@Subscribe
    public void onCtySelectEvent(CityUpdateEvent event) {
        Log.d("drawer city event", "called");
        getActivity();
        if (event.getCityName() != null) {
            userAddress.setText(event.getCityName());
        }
    }*/
}
