package com.merabreak.activities;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.ClaimedVoucherListAdapter;
import com.merabreak.adapter.ClaimedVoucherRecyclerViewAdapter;
import com.merabreak.adapter.VoucherExpendableListAdapter;
import com.merabreak.adapter.VoucherListRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.ClaimModel;
import com.merabreak.models.ClaimedVoucherModel;
import com.merabreak.models.NotificationModel;
import com.merabreak.models.VoucherModel;
import com.merabreak.models.VoucherResponseModel;
import com.merabreak.network.Connectivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ewebcoremac1 on 03/07/18.
 */

@EActivity(R.layout.activity_vouchers_list)
public class VoucherListsActivity extends BaseActivity implements VoucherListRecyclerViewAdapter.MyClickListener, ClaimedVoucherRecyclerViewAdapter.MyClickListener {

    private static String TAG = VoucherListsActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.prize_lvExp)
    ExpandableListView prizeLvExp;

    @ViewById(R.id.voucher_rv)
    RecyclerView voucherRv;

    @ViewById(R.id.claimed_voucher_rv)
    RecyclerView claimedVoucherRv;

    @ViewById(R.id.title)
    TextView title;

    @Extra
    NotificationModel notification;

    private HashMap<String, List<String>> listDataChild;
    private ArrayList<String> listDataHeader;

    VoucherExpendableListAdapter voucherExpendableListAdapter;

    VoucherListRecyclerViewAdapter voucherListRecyclerViewAdapter;
    ClaimedVoucherRecyclerViewAdapter claimedVoucherRecyclerViewAdapter;
    ClaimedVoucherListAdapter claimedVoucherListAdapter;
    private ArrayList<ClaimModel> claimList;

    private ArrayList<ClaimedVoucherModel> claimedVoucherList;
    private VoucherModel voucherModel;
    private AlertDialog alertDialog;


    @AfterViews
    void init() {
        title.setText(R.string.vouchers);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());

        /*prepareListData();
        voucherExpendableListAdapter = new VoucherExpendableListAdapter(this,listDataHeader,listDataChild);
        prizeLvExp.setAdapter(voucherExpendableListAdapter);*/

        setAdapter();

        callVoucherListAPI(notification.getNotification_id());

    }

    private void setAdapter() {

        // Set Adapter for available Vouchers

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        voucherRv.setLayoutManager(manager);
        voucherRv.setNestedScrollingEnabled(false);
        voucherRv.setHasFixedSize(false);

        claimList = new ArrayList<>();

        voucherListRecyclerViewAdapter = new VoucherListRecyclerViewAdapter(this, claimList);
        voucherListRecyclerViewAdapter.setOnItemClickListener(this);
        voucherRv.setAdapter(voucherListRecyclerViewAdapter);


        // list of claimed voucher

        /*LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);*/

        /*GridLayoutManager manager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        claimedVoucherRv.setLayoutManager(manager2);
        claimedVoucherRv.setNestedScrollingEnabled(false);
        claimedVoucherRv.setHasFixedSize(false);

        claimedVoucherList = new ArrayList<>();

        claimedVoucherListAdapter = new ClaimedVoucherListAdapter(this, claimedVoucherList);
        claimedVoucherRv.setAdapter(claimedVoucherListAdapter);*/

        // list of claimed voucher

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        claimedVoucherRv.setLayoutManager(manager2);
        claimedVoucherRv.setNestedScrollingEnabled(false);
        claimedVoucherRv.setHasFixedSize(false);


        claimedVoucherList = new ArrayList<>();
        claimedVoucherRecyclerViewAdapter = new ClaimedVoucherRecyclerViewAdapter(this, claimedVoucherList);
        claimedVoucherRecyclerViewAdapter.setOnItemClickListener(this);
        claimedVoucherRv.setAdapter(claimedVoucherRecyclerViewAdapter);

    }

    private void prepareListData() {
        listDataHeader  = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void claimDialogPopup(String title, String msg, boolean isGotVoucherCode) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_claim, null);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView appUpdateTitle = (TextView) promptView.findViewById(R.id.dialog_title);
        TextView appUpdateDesc = (TextView) promptView.findViewById(R.id.dialog_desc);
        Button appUpdate = (Button) promptView.findViewById(R.id.dialog_action_btn);
        ImageView imgView = (ImageView) promptView.findViewById(R.id.imageViewOverlay);

        if(!isGotVoucherCode)
        {
            imgView.setImageResource(R.drawable.almost_there_icon);
        }

        appUpdateTitle.setTypeface(font);

        try {
            appUpdateTitle.setText(title);
        }catch (NullPointerException e) {

            Log.e(TAG,e.toString());
        }
        try {
            appUpdateDesc.setText(msg);
        }catch (NullPointerException e) {

            Log.e(TAG,e.toString());
        }


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        appUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isGotVoucherCode)
                {
                    alertDialog.dismiss();
                    finish();
                }else
                {
                    alertDialog.dismiss();
                    claimList.clear();
                    callVoucherListAPI(notification.getNotification_id());
                }

            }
        });
        alertDialog.setView(promptView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public void onItemClick(ClaimModel position, View v) {

    }

    @Override
    public void onItemClick(ClaimedVoucherModel position, View v) {

    }

    @Override
    public void onItemClick(View v, String tag, int position) {



        callClaimAPI(claimList.get(position),notification.getNotification_id(),notification.getType());

    }

    private void callClaimAPI(ClaimModel claimModel, int notificationId, String notificationType ) {

        if (Connectivity.isConnectedWifi(context)) {
            Call<APIResponse<VoucherResponseModel>> callback = apiService.claimPrize(Constants.MB_API_KEY,
                    user.getAuthToken(),
                    claimModel.getClaim_id(),
                    notificationId,
                    notificationType);
            callback.enqueue(new Callback<APIResponse<VoucherResponseModel>>() {
                @Override
                public void onResponse(Response<APIResponse<VoucherResponseModel>> response) {
                    //hideLoader(progress);
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(VoucherListsActivity.this, user);
                                    callClaimAPI(claimModel, notificationId, notificationType);
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {

                                        claimDialogPopup(response.body().getValues().getVoucher_code(), response.body().getMeta().getMessage(), true);

                                    }
                                }
                                else {
                                    claimDialogPopup("Uh-Uh!", response.body().getMeta().getMessage(),false);
                                }
                            }
                        } else {
                            Util.makeToast(VoucherListsActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(VoucherListsActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    }

    private void callVoucherListAPI(int notificationId) {

        if (Connectivity.isConnectedWifi(context)) {
            voucherRv.setVisibility(View.VISIBLE);
            //placeHolder.setVisibility(View.GONE);

            Call<APIResponse<VoucherModel>> callback = apiService.getVoucherList(Constants.MB_API_KEY, user.getAuthToken(),String.valueOf(notificationId));
            callback.enqueue(new Callback<APIResponse<VoucherModel>>() {
                @Override
                public void onResponse(Response<APIResponse<VoucherModel>> response) {
                    //hideLoader(progress);
                    if (response.body() != null && response.code() == 200) {
                        if (response.body().getMeta() != null) {
                            if (response.body().getMeta().getStatusCode() == 401) {
                                if (response.body().getMeta().isStatus()) {
                                    user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                    Util.setUser(VoucherListsActivity.this, user);
                                    callVoucherListAPI(notificationId);
                                } else {
                                    oopsLogout(response.body().getMeta().getMessage());
                                }
                            } else {
                                if (response.body().getMeta().isStatus()) {
                                    if (response.body().getValues() != null) {
                                        voucherModel = response.body().getValues();
                                        if(voucherModel.getClaim_status() !=null &&  voucherModel.getClaim_status().toString().trim().equals("yes"))
                                        {
                                            if(response.body().getValues().getClaim_voucher().size() > 0)
                                            {
                                                claimedVoucherRv.setVisibility(View.VISIBLE);
                                                voucherRv.setVisibility(View.GONE);
                                                // placeHolder.setVisibility(View.GONE);

                                                claimedVoucherList.addAll(response.body().getValues().getClaim_voucher());
                                               // claimedVoucherListAdapter.notifyDataSetChanged();

                                                claimedVoucherRecyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        else
                                        {
                                            if(response.body().getValues().getClaim_list().size() > 0)
                                            {
                                                voucherRv.setVisibility(View.VISIBLE);
                                                claimedVoucherRv.setVisibility(View.GONE);
                                                // placeHolder.setVisibility(View.GONE);

                                                claimList.addAll(response.body().getValues().getClaim_list());


                                                voucherListRecyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                    else if (response.body().getValues() != null && claimList.size() == 0) {
                                      //  placeHolder.setVisibility(View.VISIBLE);
                                        voucherRv.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } else {
                            Util.makeToast(VoucherListsActivity.this, Constants.SOME_THING_WRONG);
                        }
                    } else {
                        Util.makeToast(VoucherListsActivity.this, Constants.SOME_THING_WRONG);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }else {
            voucherRv.setVisibility(View.GONE);
            // placeHolder.setVisibility(View.VISIBLE);
            Util.makeToast(context, context.getResources().getString(R.string.no_internet));
        }

    }

}


