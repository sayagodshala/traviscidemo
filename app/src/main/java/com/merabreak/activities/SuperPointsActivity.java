package com.merabreak.activities;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.MainActivity;
import com.merabreak.R;
import com.merabreak.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by ETPL-002 on 30-Nov-17.
 */
@EActivity(R.layout.super_points_activity)
public class SuperPointsActivity extends BaseActivity{

    AlertDialog alertDialog;
    String superPoints;

    @AfterViews
    void init(){
        superPoints = String.valueOf(Util.getUserAccountDetails(this).getPromCoins());
        proPointsAlert();
    }

    private void proPointsAlert(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.pro_point_dialog, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView points_congrats = (TextView) promptView.findViewById(R.id.points_congrats);
        TextView points_congrat_name = (TextView) promptView.findViewById(R.id.points_congrat_name);
        TextView points_congrat_desc = (TextView) promptView.findViewById(R.id.points_congrat_desc);
        Button points_congrats_redeem = (Button) promptView.findViewById(R.id.points_congrats_redeem);
        Button points_congrats_skip = (Button) promptView.findViewById(R.id.points_congrats_skip);
        points_congrat_name.setText(user.getFullName());
        points_congrat_desc.setText(Html.fromHtml(getResources().getString(R.string.pro_points_won_text) + " <b><font color=\"#593b89\">" + superPoints + "</font></b> " + getResources().getString(R.string.pro_points_won_text_extra)));
        points_congrats.setTypeface(font);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        points_congrats_redeem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RedeemPointsActivity_.intent(SuperPointsActivity.this).start();
                alertDialog.dismiss();
                finish();
            }
        });

        points_congrats_skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
}
