package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Challenge;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

/**
 * Created by Vinay on 8/16/2016.
 */
public class FeaturedChallengeViewAdapter extends RecyclerView.Adapter<FeaturedChallengeViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    public boolean offline = false;
    private Challenge mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener, View.OnLongClickListener {
        TextView type, title, category, coins_required, duration, coins, plus;
        ImageView image, offline;
        RelativeLayout deal;

        public DataObjectHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            title = (TextView) itemView.findViewById(R.id.title);
            category = (TextView) itemView.findViewById(R.id.category);
            coins_required = (TextView) itemView.findViewById(R.id.coins_required);
            duration = (TextView) itemView.findViewById(R.id.duration);
            coins = (TextView) itemView.findViewById(R.id.coins);
            image = (ImageView) itemView.findViewById(R.id.image);
            offline = (ImageView) itemView.findViewById(R.id.offline);
            plus = (TextView) itemView.findViewById(R.id.plus);
            deal = (RelativeLayout) itemView.findViewById(R.id.deal);
            itemView.setOnClickListener(this);
            offline.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                switch (v.getId()) {
                    case R.id.offline:
                        myClickListener.onOfflineClick(getPosition(), v);
                        break;
                    default:
                        myClickListener.onItemClick(getPosition(), v);
                        break;
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (myClickListener != null) {
                myClickListener.onLongItemClick(getPosition(), v);
                return true;
            }
            return false;
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public FeaturedChallengeViewAdapter(Challenge myDataset, Context context) {
        mImageLoader = new ImageLoader(context);
        mDataset = myDataset;
    }

    public FeaturedChallengeViewAdapter(Challenge myDataset, Context context, boolean offline) {
        mImageLoader = new ImageLoader(context);
        mDataset = myDataset;
        this.offline = offline;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item_challange, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        Challenge item = mDataset;

        if (!offline) {
            if (item.getOffline() == 1)
                holder.offline.setVisibility(View.VISIBLE);
            else
                holder.offline.setVisibility(View.GONE);
        }

        holder.plus.setVisibility(View.GONE);
        holder.deal.setVisibility(View.GONE);

        if (item.getCategory().size() > 0)
            holder.type.setText(item.getCategory().get(0).getTitle());
        else
            holder.type.setVisibility(View.GONE);

        holder.title.setText(item.getTitle());
        if (item.getCategory() != null && item.getCategory().size() > 0)
            holder.category.setText(item.getCategory().get(0).getTitle());
        holder.coins_required.setText(item.getCoinsRequire());
        holder.coins.setText(item.getCoins() + "");

        if (item.getBackgroundImage() != null && item.getBackgroundImage().contains("http")) {

            Picasso.with(context).load(item.getCoverImage()).placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }

        int timeLimit = item.getTimeLimit();

        if (timeLimit > 0) {
            long totalMillis = timeLimit * 1000;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));
            holder.duration.setText(hms);
        } else {
            holder.duration.setText("0");
        }
    }

    public void addItem(Challenge dataObj, int index) {
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.describeContents();
        //return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

        public void onLongItemClick(int position, View v);

        public void onOfflineClick(int position, View v);
    }
}
