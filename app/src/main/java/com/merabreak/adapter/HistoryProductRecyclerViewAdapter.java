package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Store;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryProductRecyclerViewAdapter extends RecyclerView
        .Adapter<HistoryProductRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    private List<Store> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title, price;
        TextView coins;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            coins = (TextView) itemView.findViewById(R.id.coins);
            price = (TextView) itemView.findViewById(R.id.price);
            image = (ImageView) itemView.findViewById(R.id.image);
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

    public HistoryProductRecyclerViewAdapter(List<Store> myDataset, Context context) {
        this.context = context;
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history_product, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Store item = mDataset.get(position);
        holder.title.setText(item.getTitle());
        holder.coins.setText(item.getCoins());
        holder.price.setText("Rs." + item.getPrice() + "/-");
        if (item.getImage() != null && item.getImage().contains("http")) {
            Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }
    }

    public void addItem(Store dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}