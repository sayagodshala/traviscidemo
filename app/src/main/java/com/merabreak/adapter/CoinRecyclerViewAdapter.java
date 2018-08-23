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
import com.merabreak.models.Coin;

import java.util.List;

public class CoinRecyclerViewAdapter extends RecyclerView
        .Adapter<CoinRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "CoinRecyclerViewAdapter";
    private List<Coin> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView coins, event, action, date, regRefDetails;

        public DataObjectHolder(View itemView) {
            super(itemView);
            coins = (TextView) itemView.findViewById(R.id.coins);
            event = (TextView) itemView.findViewById(R.id.event);
            action = (TextView) itemView.findViewById(R.id.action);
            date = (TextView) itemView.findViewById(R.id.date);
            regRefDetails = (TextView) itemView.findViewById(R.id.refer_details);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CoinRecyclerViewAdapter(List<Coin> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history_coin, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Coin item = mDataset.get(position);
        holder.regRefDetails.setVisibility(View.GONE);
        holder.coins.setText(item.getCoins() +" "+ context.getString(R.string.points));
        holder.event.setText(item.getEvent());
        holder.action.setText(item.getAction());
        holder.date.setText(item.getEventDate());
        if (item.getRegRefMobile() != null && item.getRegRefName() != null) {
            holder.regRefDetails.setVisibility(View.VISIBLE);
            holder.regRefDetails.setText(item.getRegRefName() + "(" + item.getRegRefMobile() + ")");
        }
    }

    public void addItem(Coin dataObj, int index) {
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
        public void onItemClick(int position, View v);
    }
}