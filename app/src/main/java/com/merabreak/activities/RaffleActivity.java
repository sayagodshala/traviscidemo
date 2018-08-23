package com.merabreak.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.FontUtils;
import com.merabreak.MainActivity_;
import com.merabreak.OperatorDialog;
import com.merabreak.OperatorDialog_;
import com.merabreak.R;
import com.merabreak.TermsDialog;
import com.merabreak.TermsDialog_;
import com.merabreak.Util;
import com.merabreak.models.APIResponse;
import com.merabreak.models.AccountDetails;
import com.merabreak.models.Operators;
import com.merabreak.models.Raffle;
import com.merabreak.models.TermsAndConditions;
import com.merabreak.models.challenge.Challenge;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by sayagodshala on 25/01/16.
 */
@EActivity(R.layout.raffle)
public class RaffleActivity extends BaseActivity {

    @Extra
    String id;

    Raffle raffle;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.brand)
    TextView brand;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.product_price)
    TextView product_price;

    @ViewById(R.id.tickets)
    TextView tickets;

    @ViewById(R.id.coins)
    TextView coins;

    @ViewById(R.id.free_shipping)
    TextView freeShipping;

    @ViewById(R.id.image)
    ImageView image;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.spec_label)
    TextView specLabel;

    @ViewById(R.id.time_left)
    TextView time_left;

    @ViewById(R.id.raffle_timer)
    TextView raffle_timer;

    @ViewById(R.id.tickets_purchased_by_user)
    TextView tickets_purchased_by_user;

    @ViewById(R.id.tickets_purchased_overall)
    TextView tickets_purchased_overall;

    @ViewById(R.id.counts)
    TextView counts;

//    @ViewById(R.id.banner)
//    LinearLayout banner;

    @ViewById(R.id.specs)
    LinearLayout specs;

    @ViewById(R.id.terms_link)
    TextView terms_link;

    @ViewById(R.id.shipping_layout)
    LinearLayout shipping_layout;

    @ViewById(R.id.info)
    NestedScrollView info;

    @ViewById(R.id.buy)
    Button buy;

    AccountDetails accountDetails;
    private int counter = 0;

    @AfterViews
    void init() {
        accountDetails = Util.getUserAccountDetails(this);
        buy.setVisibility(View.GONE);
        title.setText("");
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getRaffleDetails();

        if (android.os.Build.VERSION.SDK_INT >= 21)
            Util.changeSystemBarColor(this, R.color.white_transparent);
    }

    private void getRaffleDetails() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse<Raffle>> callback = apiService.getRaffleDetails(Constants.MB_API_KEY, user.getAuthToken(), id);
        callback.enqueue(new Callback<APIResponse<Raffle>>() {
            @Override
            public void onResponse(Response<APIResponse<Raffle>> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().isStatus()) {
                            if (response.body().getValues() != null) {
                                raffle = response.body().getValues();
                                //Log.d("Raffle Detail", new Gson().toJson(raffle));
                                setData();
                            } else {
                                Util.makeToast(RaffleActivity.this, getResources().getString(R.string.raffle_empty));
                            }
                        } else {
                            Util.makeToast(RaffleActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t != null)
                    Log.d("JSONError", t.getMessage());

                Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @Click(R.id.decrement)
    void ticketDecrement() {
        counter--;
        if (counter < 0)
            counter = 0;
        counts.setText("" + counter + "");
    }

    @Click(R.id.increment)
    void ticketIncrement() {
        counter++;
        int remainingTickets = Integer.parseInt(raffle.getRemainingTicketLimit());
        if (counter > remainingTickets)
            counter = remainingTickets;
        counts.setText("" + counter + "");
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        return formatter.format(d);
    }

    private void setData() {
        buy.setVisibility(View.VISIBLE);
        int totalTicketsPurchased = Integer.parseInt(raffle.getMaxTickets()) - Integer.parseInt(raffle.getRemainingTicketLimit());
        name.setText(raffle.getTitle());
        coins.setText(doubleToStringNoDecimal(Integer.parseInt(raffle.getCoinsPerTickets())) + " " + getResources().getString(R.string.raffle_points));
        description.setText(raffle.getDescription());
        brand.setText(raffle.getBrandName());
        tickets.setText(raffle.getMaxTickets());
        tickets_purchased_by_user.setText("" + totalTicketsPurchased + "");
        tickets_purchased_overall.setText(raffle.getTotalRafflesPurchased());
        product_price.setText(doubleToStringNoDecimal(Integer.parseInt(raffle.getCoinsPerTickets())) + " " + getResources().getString(R.string.raffle_points) + " (" + doubleToStringNoDecimal(Integer.parseInt(raffle.getPrice())) + "" + " Rs/-)");

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(raffle.getTitle());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        // Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat_Regular.ttf");
        collapsingToolbar.setExpandedTitleTypeface(font);
        collapsingToolbar.setCollapsedTitleTypeface(font);
        Picasso.with(context).load(raffle.getDetail_image()).into(image, new com.squareup.picasso.Callback.EmptyCallback() {
            @Override
            public void onSuccess() {

            }
        });

        if (raffle.getSpecification() != null && raffle.getSpecification().size() > 0) {
            for (int i = 0; i < raffle.getSpecification().size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.list_item_spec, null);
                FontUtils.setDefaultFont(this, view);
                TextView key = (TextView) view.findViewById(R.id.key);
                TextView value = (TextView) view.findViewById(R.id.value);
                key.setText(raffle.getSpecification().get(i).getSpecField());
                value.setText(raffle.getSpecification().get(i).getSpecValue());
                specs.addView(view);
            }
        } else {
            specLabel.setVisibility(View.GONE);
        }

        if (raffle.getTerms() != null && raffle.getTerms().size() > 0)
            terms_link.setVisibility(View.VISIBLE);
        else
            terms_link.setVisibility(View.GONE);


        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date raffleDate = dateFormatter.parse(raffle.getEndDateTime());
            Calendar a = Calendar.getInstance();
            a.setTime(raffleDate);

            Calendar b = Calendar.getInstance();

            long c = daysBetween(b, a);

           /* if (c < 10)
                time_left.setText("0" + c +" "+ getResources().getString(R.string.raffle_days_left));
            else
                time_left.setText(c +" "+ getResources().getString(R.string.raffle_days_left));*/
            new CountDownTimer(c, 1000) {
                public void onTick(long millisUntilFinished) {
                    // String hms = String.format("%02dd:%02dh:%02dm", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    String hms = String.format("%02dd:%02dh:%02dm:%02ds", TimeUnit.MILLISECONDS.toDays(millisUntilFinished), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    raffle_timer.setText(hms);
                }

                public void onFinish() {
                    raffle_timer.setText(R.string.main_spin_wait_close);
                }
            }.start();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.terms_link)
    void termsLink() {
       showTermsDialog((ArrayList<TermsAndConditions>) raffle.getTerms());
    }

    private void showTermsDialog(ArrayList<TermsAndConditions> list) {
        TermsDialog_.builder().arg("TERMS_CONDITIONS", list).build()
                .show(getSupportFragmentManager(), TermsDialog.class.getSimpleName());
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMillis(Math.abs(end - start));
    }

    @Click(R.id.buy)
    void onBuyRaffle() {
        if (Util.isUserLoggedIn(RaffleActivity.this)) {
            if (Integer.parseInt(counts.getText().toString()) > 0)
                buyRaffle();
            else
                Util.makeToast(RaffleActivity.this, getResources().getString(R.string.raffle_no_quantity));
        } else {
            Util.makeToast(RaffleActivity.this, getResources().getString(R.string.raffle_not_sign_up));
        }
    }

    private void buyRaffle() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.purchaseRaffle(Constants.MB_API_KEY, user.getAuthToken(), id, counts.getText().toString());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.code() == 200 && response.body() != null) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(RaffleActivity.this, user);
                                buyRaffle();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else if (response.body().getMeta().getStatusCode() == 200) {
                            if (response.body().getMeta().isStatus()) {
                                Util.makeToast(RaffleActivity.this, response.body().getMeta().getMessage());
                                finish();
                            } else {
                                Util.makeToast(RaffleActivity.this, response.body().getMeta().getMessage());
                            }
                        } else {
                            Util.makeToast(RaffleActivity.this, response.body().getMeta().getMessage());
                        }
                    } else {
                        Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t != null)
                    Log.d("JSONError", t.getMessage());

                Util.makeToast(RaffleActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

   /* @Click(R.id.image)
    void onImageClick() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (raffle.getImage().contains("http") || raffle.getImage().contains("https")) {
            intent.setDataAndType(
                    Uri.parse(raffle.getDetail_image()),
                    "image*//*");
        }
        startActivity(intent);
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }
}
