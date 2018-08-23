package com.merabreak.adapter;

import android.app.Activity;
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
import com.merabreak.models.Cities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vinay on 12/13/2016.
 */
public class CitysRecyclerViewAdapter extends RecyclerView
        .Adapter<CitysRecyclerViewAdapter
        .DataObjectHolder> {

    public List<Cities> mDataset;
    private static MyClickListener myClickListener;
    private Activity listContext;
    Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView cityName;
        ImageView cityImage;
        View active;

        public DataObjectHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.city_name);
            cityImage = (ImageView) itemView.findViewById(R.id.city_image);
            active = (View) itemView.findViewById(R.id.active);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CitysRecyclerViewAdapter(List<Cities> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_items, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        Cities item = mDataset.get(position);

        if (item.getCitySelection() == 1)
            holder.active.setVisibility(View.VISIBLE);

        holder.cityName.setText(item.getCityName());

        if (item.getCityImage() != null && item.getCityImage().contains("http")) {
            Picasso.with(context).load(item.getCityImage()).placeholder(R.drawable.dummy_challenge_image).into(holder.cityImage, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //Log.d("Data Size", mDataset.size() + "");
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(Cities position, View v);
    }
}
