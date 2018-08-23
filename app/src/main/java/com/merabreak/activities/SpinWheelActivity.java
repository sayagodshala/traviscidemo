package com.merabreak.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MainActivity;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.games.spinwheel.WheelView;
import com.merabreak.models.APIResponse;
import com.merabreak.models.PointsWallet;
import com.merabreak.models.SpinWheelDetails;
import com.merabreak.models.SpinWheelIndex;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ETPL-002 on 03-Oct-17.
 */
@EActivity(R.layout.spin_wheel)
public class SpinWheelActivity extends BaseActivity {

    @ViewById(R.id.spinWheel)
    WheelView wheelView;

    @ViewById(R.id.rotate_wheel)
    Button rotateWheel;

    @ViewById(R.id.button_close)
    Button closeBtn;

    @ViewById(R.id.spin_wheel_close)
    ImageButton closeImgBtn;

    @ViewById(R.id.spin_wheel_back)
    ImageView spinWheelBack;

    @ViewById(R.id.spin_wheel_effect)
    ImageView spinWheelEffect;

    @ViewById(R.id.spin_wheel_arrow)
    ImageView spinWheelArrow;

    Animation animation;
    AlertDialog alertDialog;

    @Extra
    SpinWheelDetails spinWheelDetails;

    List<SpinWheelIndex> spinWheelContent = new ArrayList<SpinWheelIndex>();

    @ViewById(R.id.progress)
    LinearLayout progress;

    @AfterViews
    void init() {
        setSpinDetails();
    }

    private void setSpinDetails(){
        spinWheelContent = new ArrayList<SpinWheelIndex>();
        SpinWheelIndex wheelItem1 = spinWheelDetails.getSpinDetails().get(0);
        String s1 = wheelItem1.getCoins();
        StringBuilder result1 = new StringBuilder();
        if (s1.length() > 0) {
            result1.append(s1.charAt(0));
            for (int i = 1; i < s1.length(); i++) {
                result1.append(" ");
                result1.append(s1.charAt(i));
            }
        }
        wheelItem1.setCoins(result1.toString());
        wheelItem1.setText("P o i n t s");
        wheelItem1.setIcon(R.drawable.animal);
        wheelItem1.setColor(Color.parseColor("#4fdd62"));
        spinWheelContent.add(wheelItem1);

        SpinWheelIndex wheelItem2 = spinWheelDetails.getSpinDetails().get(1);
        String s2 = wheelItem2.getCoins();
        StringBuilder result2 = new StringBuilder();
        if (s2.length() > 0) {
            result2.append(s2.charAt(0));
            for (int i = 1; i < s2.length(); i++) {
                result2.append(" ");
                result2.append(s2.charAt(i));
            }
        }
        wheelItem2.setCoins(result2.toString());
        wheelItem2.setText("P o i n t s");
        wheelItem2.setIcon(R.drawable.food_drink);
        wheelItem2.setColor(Color.parseColor("#55d3ee"));
        spinWheelContent.add(wheelItem2);

        SpinWheelIndex wheelItem3 = spinWheelDetails.getSpinDetails().get(2);
        String s3 = wheelItem3.getCoins();
        StringBuilder result3 = new StringBuilder();
        if (s3.length() > 0) {
            result3.append(s3.charAt(0));
            for (int i = 1; i < s3.length(); i++) {
                result3.append(" ");
                result3.append(s3.charAt(i));
            }
        }
        wheelItem3.setCoins(result3.toString());
        wheelItem3.setText("P o i n t s");
        wheelItem3.setIcon(R.drawable.animal);
        wheelItem3.setColor(Color.parseColor("#0e95e7"));
        spinWheelContent.add(wheelItem3);

        SpinWheelIndex wheelItem4 = spinWheelDetails.getSpinDetails().get(3);
        String s4 = wheelItem4.getCoins();
        StringBuilder result4= new StringBuilder();
        if (s4.length() > 0) {
            result4.append(s4.charAt(0));
            for (int i = 1; i < s4.length(); i++) {
                result4.append(" ");
                result4.append(s4.charAt(i));
            }
        }
        wheelItem4.setCoins(result4.toString());
        wheelItem4.setText("P o i n t s");
        wheelItem4.setIcon(R.drawable.animal);
        wheelItem4.setColor(Color.parseColor("#595df5"));
        spinWheelContent.add(wheelItem4);

        SpinWheelIndex wheelItem5 = spinWheelDetails.getSpinDetails().get(4);
        String s5 = wheelItem5.getCoins();
        StringBuilder result5 = new StringBuilder();
        if (s5.length() > 0) {
            result5.append(s5.charAt(0));
            for (int i = 1; i < s5.length(); i++) {
                result5.append(" ");
                result5.append(s5.charAt(i));
            }
        }
        wheelItem5.setCoins(result5.toString());
        wheelItem5.setText("P o i n t s");
        wheelItem5.setIcon(R.drawable.animal);
        wheelItem5.setColor(Color.parseColor("#cf85f3"));
        spinWheelContent.add(wheelItem5);

        SpinWheelIndex wheelItem6 = spinWheelDetails.getSpinDetails().get(5);
        String s6 = wheelItem6.getCoins();
        StringBuilder result6 = new StringBuilder();
        if (s6.length() > 0) {
            result6.append(s6.charAt(0));
            for (int i = 1; i < s6.length(); i++) {
                result6.append(" ");
                result6.append(s6.charAt(i));
            }
        }
        wheelItem6.setCoins(result6.toString());
        wheelItem6.setText("P o i n t s");
        wheelItem6.setIcon(R.drawable.animal);
        wheelItem6.setColor(Color.parseColor("#f55e88"));
        spinWheelContent.add(wheelItem6);

        SpinWheelIndex wheelItem7 = spinWheelDetails.getSpinDetails().get(6);
        String s7 = wheelItem7.getCoins();
        StringBuilder result7 = new StringBuilder();
        if (s7.length() > 0) {
            result7.append(s7.charAt(0));
            for (int i = 1; i < s7.length(); i++) {
                result7.append(" ");
                result7.append(s7.charAt(i));
            }
        }
        wheelItem7.setCoins(result7.toString());
        wheelItem7.setText("P o i n t s");
        wheelItem7.setIcon(R.drawable.animal);
        wheelItem7.setColor(Color.parseColor("#f68c5d"));
        spinWheelContent.add(wheelItem7);

        SpinWheelIndex wheelItem8 = spinWheelDetails.getSpinDetails().get(7);
        String s8 = wheelItem8.getCoins();
        StringBuilder result8 = new StringBuilder();
        if (s8.length() > 0) {
            result8.append(s8.charAt(0));
            for (int i = 1; i < s8.length(); i++) {
                result8.append(" ");
                result8.append(s8.charAt(i));
            }
        }
        wheelItem8.setCoins(result8.toString());
        wheelItem8.getCoins();
        wheelItem8.setText("P o i n t s");
        wheelItem8.setIcon(R.drawable.animal);
        wheelItem8.setColor(Color.parseColor("#f3a960"));
        spinWheelContent.add(wheelItem8);

        animation = AnimationUtils.loadAnimation(this, R.anim.pulse_he);
        rotateWheel.startAnimation(animation);
        wheelView.setData(spinWheelContent);
        wheelView.setRound(3);

        wheelView.setLuckyRoundItemSelectedListener(new WheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                // Util.makeToast(getApplicationContext(),String.valueOf(index));
                if (index == 1) {
                    String s = spinWheelDetails.getSpinDetails().get(0).getCoins();
                    removeSpace(s);
                    //Util.makeToast(getApplicationContext(), "Congratulations..! You got 10 IPLPointsConfig.");
                }else if (index == 2) {
                    String s = spinWheelDetails.getSpinDetails().get(1).getCoins();
                    removeSpace(s);
                  //  Util.makeToast(getApplicationContext(), "Congratulations..! You got 50 IPLPointsConfig.");
                }else if (index == 3) {
                    String s = spinWheelDetails.getSpinDetails().get(2).getCoins();
                    removeSpace(s);
                 //   Util.makeToast(getApplicationContext(), "Congratulations..! You got 100 IPLPointsConfig.");
                }else if (index == 4) {
                    String s = spinWheelDetails.getSpinDetails().get(3).getCoins();
                    removeSpace(s);
                 //   Util.makeToast(getApplicationContext(), "Congratulations..! You got 500 IPLPointsConfig.");
                }else if (index == 5) {
                    String s = spinWheelDetails.getSpinDetails().get(4).getCoins();
                    removeSpace(s);
                 //   Util.makeToast(getApplicationContext(), "Congratulations..! You got 1000 IPLPointsConfig.");
                }else if (index == 6) {
                    String s = spinWheelDetails.getSpinDetails().get(5).getCoins();
                    removeSpace(s);
                 //   Util.makeToast(getApplicationContext(), "Congratulations..! You got 50 IPLPointsConfig.");
                }else if (index == 7) {
                    String s = spinWheelDetails.getSpinDetails().get(6).getCoins();
                    removeSpace(s);
                //    Util.makeToast(getApplicationContext(), "Congratulations..! You got 100 IPLPointsConfig.");
                }else if (index == 8) {
                    String s = spinWheelDetails.getSpinDetails().get(7).getCoins();
                    removeSpace(s);
                  //  Util.makeToast(getApplicationContext(), "Congratulations..! You got 500 IPLPointsConfig.");
                }else {
                    String s = spinWheelDetails.getSpinDetails().get(0).getCoins();
                    removeSpace(s);
                  //  Util.makeToast(getApplicationContext(), "Congratulations..! You got 10 IPLPointsConfig.");
                }
            }
        });
    }

    private void removeSpace(String s){
        String withoutspaces = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                withoutspaces += s.charAt(i);
        }
        sendSpinPoints(Integer.parseInt(withoutspaces));
    }

    @Click(R.id.spin_wheel_close)
    void closeSpinWindow()
    {
        finish();
    }

    @Click(R.id.button_close)
    void closeSpins()
    {
        finish();
    }

    @Click(R.id.rotate_wheel)
    void rotateWheel() {
      //  int index = getRandomIndex();
        wheelView.startLuckyWheelWithTargetIndex(spinWheelDetails.getTarget_index());
        animation.cancel();
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(spinWheelContent.size() - 1);
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    private void sendSpinPoints(int spin_points){
        int spin_id = spinWheelDetails.getSpin_id();
        Call<APIResponse> callback = apiService.sendSpinPoints(Constants.MB_API_KEY, user.getAuthToken(), spin_id, spin_points, spinWheelDetails.getTarget_index());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                //Log.d("spin points respo", new Gson().toJson(response.body().getValues()));
                try {
                    if(response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(SpinWheelActivity.this, user);
                                    sendSpinPoints(spin_points);
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                               /* final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 3000);*/
                                    proPointsAlert(spin_points);
                                } else {
                                    Util.makeToast(SpinWheelActivity.this, response.body().getMeta().getMessage());
                                }
                            }
                        } else {
                            Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    private void proPointsAlert(int spin_points){
        String points = String.valueOf(spin_points);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.spin_win_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView spin_points_congrats = (TextView) promptView.findViewById(R.id.spin_points_congrats);
        TextView spin_points_congrat_desc = (TextView) promptView.findViewById(R.id.spin_points_congrat_desc);
        Button spin_points_close = (Button) promptView.findViewById(R.id.spin_points_close);
        Button spin_points_respins = (Button) promptView.findViewById(R.id.button_respins);
        Button spinClose = (Button) promptView.findViewById(R.id.spins_close_btn);
        LinearLayout linearLayout = (LinearLayout) promptView.findViewById(R.id.respins_hlder);
        spin_points_congrat_desc.setText(Html.fromHtml(getResources().getString(R.string.spin_win_dialog_won_text) + " <b><font color=\"#593b89\">" + points + "</font></b> " + getResources().getString(R.string.spin_win_dialog_points_text)));
        spin_points_congrats.setTypeface(font);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        PointsWallet pointsWallet = Util.getPointsWallet(SpinWheelActivity.this);
        pointsWallet.setSpin_count(pointsWallet.getSpin_count()-1);
        Util.setPointWallet(SpinWheelActivity.this,pointsWallet);

        if(Util.getPointsWallet(SpinWheelActivity.this).getSpin_count() > 0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            spinClose.setVisibility(View.GONE);
        }
        else
        {
            linearLayout.setVisibility(View.GONE);
            spinClose.setVisibility(View.VISIBLE);
        }


        spinClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        spin_points_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        spin_points_respins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Util.getPointsWallet(SpinWheelActivity.this).getSpin_count() > 0)
                {
                    alertDialog.dismiss();
                    getSpinDetails();
                  //  Util.getPointsWallet(SpinWheelActivity.this).setSpin_count(Util.getPointsWallet(SpinWheelActivity.this).getSpin_count()-1);
                }
                else
                {
                    alertDialog.dismiss();
                    finish();
                }
                /*alertDialog.dismiss();
                rotateWheel();*/
            }
        });

        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void getSpinDetails() {
        if (Connectivity.isConnected(context)) {
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
                                        Util.setUser(SpinWheelActivity.this, user);
                                        getSpinDetails();
                                    } else {
                                        oopsLogout(response.body().getMeta().getMessage());
                                    }
                                } else {
                                    if (response.body().getMeta().isStatus()) {
                                        if (response.body().getValues() != null) {
                                            spinWheelDetails = response.body().getValues();
                                            setSpinDetails();
                                        } else {
                                            Util.makeToast(SpinWheelActivity.this, response.body().getMeta().getMessage());
                                        }
                                    } else {
                                        Util.makeToast(SpinWheelActivity.this, response.body().getMeta().getMessage());
                                    }
                                }
                            } else {
                                Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
                            }
                        } else {
                            Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    hideLoader(progress);
                    Util.makeToast(SpinWheelActivity.this, Constants.SOME_THING_WRONG);
                }
            });
        }else
        {
            Util.makeToast(context, context.getResources().getString(R.string.challenge_adapter_no_internet));
            finish();
        }
    }


}
