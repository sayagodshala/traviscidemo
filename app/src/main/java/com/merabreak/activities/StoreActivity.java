package com.merabreak.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.MainActivity_;
import com.merabreak.R;
import com.merabreak.TermsDialog;
import com.merabreak.TermsDialog_;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Store;
import com.merabreak.models.TermsAndConditions;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by sayagodshala on 25/01/16.
 */
@EActivity(R.layout.store_detail)
public class StoreActivity extends BaseActivity {

    @Extra
    String id;

    Store store;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.brand)
    TextView brand;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.coins)
    TextView coins;

    @ViewById(R.id.terms_link)
    TextView terms_link;

    @ViewById(R.id.product_price)
    TextView product_price;

    @ViewById(R.id.free_shipping)
    TextView freeShipping;

    @ViewById(R.id.image)
    ImageView image;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.spec_label)
    TextView specLabel;

    @ViewById(R.id.specs)
    LinearLayout specs;

    @ViewById(R.id.shipping_layout)
    LinearLayout shipping_layout;

    @ViewById(R.id.info)
    NestedScrollView info;

    @ViewById(R.id.buy)
    Button buy;

    AccountDetails accountDetails;
    List<TermsAndConditions> termsAndConditions;

    @AfterViews
    void init() {
        accountDetails = Util.getUserAccountDetails(this);
        buy.setVisibility(View.GONE);
        title.setText("");
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getStoreDetail();

        if (android.os.Build.VERSION.SDK_INT >= 21)
            Util.changeSystemBarColor(this, R.color.white_transparent);
    }

    private void getStoreDetail() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<Store>> callback = apiService.getStoreDetails(Constants.MB_API_KEY, user.getAuthToken(), id);
        callback.enqueue(new Callback<APIResponse<Store>>() {
            @Override
            public void onResponse(Response<APIResponse<Store>> response) {
                hideLoader(progress);
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null) {
                                store = response.body().getValues();
                                //Log.d("Store Detail", new Gson().toJson(store));
                                termsAndConditions = store.getTerms();
                                setData();
                            } else {
                                Util.makeToast(StoreActivity.this, getResources().getString(R.string.store_no_products));
                            }
                        } else {
                            Util.makeToast(StoreActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
              //  if (t != null)
                //    Log.d("JSONError", t.getMessage());
                Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }

    private void setData() {
        buy.setVisibility(View.VISIBLE);
        if (store.getProductType().equalsIgnoreCase("physical")) {
            freeShipping.setText(R.string.store_product_shipping);
        } else {
            shipping_layout.setVisibility(View.GONE);
        }
        name.setText(store.getTitle());
        description.setText(store.getDescription().trim());
        coins.setText(doubleToStringNoDecimal(Integer.parseInt(store.getCoins())) +" "+ getResources().getString(R.string.store_product_points));
        brand.setText(store.getBrandName());
        product_price.setText(doubleToStringNoDecimal(Integer.parseInt(store.getCoins())) +" "+ getResources().getString(R.string.store_product_points) + " (" + doubleToStringNoDecimal(Integer.parseInt(store.getPrice())) +""+ " Rs/-)");
        Picasso.with(context).load(store.getDetail_image()).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
            }
        });

        if (store.getTerms() != null && store.getTerms().size() > 0)
            terms_link.setVisibility(View.VISIBLE);
        else
            terms_link.setVisibility(View.GONE);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(store.getTitle());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        //Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat_Regular.ttf");
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        collapsingToolbar.setExpandedTitleTypeface(font);
        collapsingToolbar.setCollapsedTitleTypeface(font);
        if (store.getSpecification() != null && store.getSpecification().size() > 0) {
            for (int i = 0; i < store.getSpecification().size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.list_item_spec, null);
                FontUtils.setDefaultFont(this, view);
                TextView key = (TextView) view.findViewById(R.id.key);
                TextView value = (TextView) view.findViewById(R.id.value);
                key.setText(store.getSpecification().get(i).getSpecField());
                value.setText(store.getSpecification().get(i).getSpecValue());
                specs.addView(view);
            }
        } else {
            specLabel.setVisibility(View.GONE);
        }
    }

    @Click(R.id.terms_link)
    void termsLink() {
        showTermsDialog((ArrayList<TermsAndConditions>) store.getTerms());
    }

    private void showTermsDialog(ArrayList<TermsAndConditions> list) {
        TermsDialog_.builder().arg("TERMS_CONDITIONS", list).build()
                .show(getSupportFragmentManager(), TermsDialog.class.getSimpleName());
    }

    @Click(R.id.buy)
    void onBuyStore() {
        if (store.getProductType().equalsIgnoreCase("downloadable")) {
            if (Util.isUserLoggedIn(StoreActivity.this)) {
                buyStore();
            } else {
                Util.makeToast(StoreActivity.this, getResources().getString(R.string.store_user_not_reg));
            }
        } else if (store.getProductType().equalsIgnoreCase("subscription")) {
            if (Util.isUserLoggedIn(StoreActivity.this)) {
                buyStore();
            } else {
                Util.makeToast(StoreActivity.this, getResources().getString(R.string.store_user_not_reg));
            }
        } else if (store.getProductType().equalsIgnoreCase("physical")) {
            ShippingAddressListActivity_.intent(this).store(store).start();
        } else {

        }
    }

    private void buyStore() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.purchaseProduct(Constants.MB_API_KEY, user.getAuthToken(), store.getId());
        callback.enqueue(new Callback<APIResponse>() {
                             @Override
                             public void onResponse(Response<APIResponse> response) {
                                 hideLoader(progress);
                                 if(response.code() == 200 && response.body() != null) {
                                     if (response.body().getMeta() != null) {
                                         if (response.body().getMeta().getStatusCode() == 401) {
                                             if (response.body().getMeta().isStatus()) {
                                                 user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                                 Util.setUser(StoreActivity.this, user);
                                                 buyStore();
                                             } else {
                                                 oopsLogout(response.body().getMeta().getMessage());
                                             }
                                         } else if (response.body().getMeta().getStatusCode() == 200) {
                                             if (response.body().getMeta().isStatus()) {
                                                 Util.makeToast(StoreActivity.this, response.body().getMeta().getMessage());
                                                 AccountDetails ad = Util.getUserAccountDetails(StoreActivity.this);
                                                 ad.setTotalProducts(ad.getTotalProducts() + 1);
                                                 Util.saveUserAccountDetails(StoreActivity.this, ad);
                                                 finish();
                                             } else {
                                                 Util.makeToast(StoreActivity.this, response.body().getMeta().getMessage());
                                             }
                                         } else {
                                             Util.makeToast(StoreActivity.this, response.body().getMeta().getMessage());
                                         }
                                     } else {
                                         Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
                                     }
                                 } else {
                                     Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
                                 }
                             }

                             @Override
                             public void onFailure(Throwable t) {
                                 hideLoader(progress);
                                 //if (t.getMessage() != null)
                                //     Log.d("JSONError", t.getMessage());
                                 Util.makeToast(StoreActivity.this, Constants.SOME_THING_WRONG);
                             }
                         }

        );
    }

   /* @Click(R.id.image)
    void onImageClick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (store.getImage().contains("http") || store.getImage().contains("https")) {
            intent.setDataAndType(
                    Uri.parse(store.getDetail_image()),
                    "image*//*");
        }
        startActivity(intent);
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }
}
