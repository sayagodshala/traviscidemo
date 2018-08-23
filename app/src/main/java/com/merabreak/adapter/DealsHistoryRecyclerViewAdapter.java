package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.challenge.Challenge;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vinay on 12/2/2016.
 */
public class DealsHistoryRecyclerViewAdapter extends RecyclerView.Adapter<DealsHistoryRecyclerViewAdapter.DataObjectHolder> {

    public List<Challenge> mDataset;
    private static MyClickListener myClickListener;
    Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title, validity, voucherCode, amount;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            validity = (TextView) itemView.findViewById(R.id.validity);
            title = (TextView) itemView.findViewById(R.id.title);
            voucherCode = (TextView) itemView.findViewById(R.id.voucher_code);
            amount = (TextView) itemView.findViewById(R.id.coins);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(getPosition(), v);
            }
        }
    }

    public DealsHistoryRecyclerViewAdapter(List<Challenge> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_deals_recycler_item, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Challenge item = mDataset.get(position);

        holder.title.setText(item.getDealTitle());
        holder.validity.setText(context.getString(R.string.valid_till) +" "+ item.getDealValidity());
        holder.voucherCode.setText(" "+item.getDealCode()+"");
        if (item.getDealAmount() != null) {
            holder.amount.setVisibility(View.VISIBLE);
            holder.amount.setText(item.getDealAmount());
        }

        if (item.getDealImage() != null && item.getDealImage().contains("http")) {
            Picasso.with(context).load(item.getDealImage()).placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }
    }

    public void addItem(Challenge dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
       // Log.d("Data Size", mDataset.size() + "");
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

        public void onLongItemClick(int position, View v);

        public void onOfflineClick(int position, View v);
    }
}
