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
import com.merabreak.models.challenge.Winner;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vinay on 8/31/2016.
 */
public class WinnersRecyclerViewAdapter extends RecyclerView.Adapter<WinnersRecyclerViewAdapter.DataObjectHolder> {

    private List<Winner> mDataset;
    private Context context;
    private ImageLoader mImageLoader;

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView winnerName;
        RoundedImageView winnerImage;

        public DataObjectHolder(View itemView) {
            super(itemView);
            winnerName = (TextView) itemView.findViewById(R.id.winner_name);
            winnerImage = (RoundedImageView) itemView.findViewById(R.id.winner_image);
        }
    }

    public WinnersRecyclerViewAdapter(List<Winner> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.winners_list, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Winner item = mDataset.get(position);
        holder.winnerName.setText(item.getWinnerName());
        if (item.getWinnerImage() != null && item.getWinnerImage().contains("http"))
            Picasso.with(context).load(item.getWinnerImage()).placeholder(R.drawable.dummy_user_icon).into(holder.winnerImage);
    }

    public void addItem(Winner dataObj, int index) {
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
}
