package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.RechargePlans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vinay on 7/26/2016.
 */
public class MobileRechargeViewAdapter extends RecyclerView.
        Adapter<MobileRechargeViewAdapter.
        DataObjectHolder> {

    private static String LOG_TAG = "MobileRechargeViewAdapter";
    private List<RechargePlans> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView coins, recharge_amount, recharge_plan_title, recharge_plan_desc, recharge_plan_validity;

        public DataObjectHolder(View itemView) {
            super(itemView);
            coins = (TextView) itemView.findViewById(R.id.coins);
            recharge_amount = (TextView) itemView.findViewById(R.id.recharge_amount);
            recharge_plan_title = (TextView) itemView.findViewById(R.id.recharge_plan_title);
            recharge_plan_desc = (TextView) itemView.findViewById(R.id.recharge_plan_desc);
            recharge_plan_validity = (TextView) itemView.findViewById(R.id.recharge_plan_validity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MobileRechargeViewAdapter(List<RechargePlans> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recharge_plans, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        RechargePlans item = mDataset.get(position);
        holder.coins.setText(doubleToStringNoDecimal(Integer.parseInt(item.getCoins())));
        holder.recharge_amount.setText("Rs." + item.getAmount() + "/-");
        holder.recharge_plan_title.setText(item.getDescription());
        holder.recharge_plan_validity.setText(item.getValidity());
    }

    public void addItem(RechargePlans dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        //Log.d("Data Size", mDataset.size() + "");

        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(RechargePlans position, View v);
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }
}
