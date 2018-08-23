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
import com.merabreak.Util;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.User;
import com.merabreak.models.challenge.Astrology;
import com.merabreak.models.challenge.Category;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AstrologyRecyclerViewAdapter extends RecyclerView
        .Adapter<AstrologyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    public List<Astrology> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    public User user;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title, subTitle;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subDate);
            image = (ImageView) itemView.findViewById(R.id.image);
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

    public AstrologyRecyclerViewAdapter(List<Astrology> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item_astrology, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        user = Util.getUser(context);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Astrology item = mDataset.get(position);
        holder.title.setText(item.getSignName());
        holder.subTitle.setText(item.getSignDate());
        if (item.getCoverImage() != null && item.getCoverImage().contains("http")) {
            Picasso.with(context).load(item.getCoverImage()).fit().centerCrop().placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }
    }

    public void addItem(Astrology dataObj, int index) {
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
        public void onItemClick(Astrology position, View v);
    }
}