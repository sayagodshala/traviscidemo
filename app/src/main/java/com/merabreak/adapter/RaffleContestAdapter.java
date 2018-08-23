package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Cities;
import com.merabreak.models.challenge.Challenge;
import com.merabreak.models.challenge.Winner;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RaffleContestAdapter extends RecyclerView.Adapter<RaffleContestAdapter.DataObjectHolder> {

    private List<Challenge> mDataset;
    private Context context;
    private ImageLoader mImageLoader;
    private static MyClickListener myClickListener;

    public static MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(MyClickListener myClickListener) {
        RaffleContestAdapter.myClickListener = myClickListener;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView itemImg;
        RelativeLayout alreadyPlayedHldr;

        public DataObjectHolder(View itemView) {
            super(itemView);

            itemImg = (RoundedImageView) itemView.findViewById(R.id.item_img);
            alreadyPlayedHldr = (RelativeLayout) itemView.findViewById(R.id.alrady_played_hldr);
            itemImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public RaffleContestAdapter(List<Challenge> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_raffle_contest_item, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);

        int height = parent.getHeight() - 2*(int) context.getResources().getDimension(R.dimen.dip20);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(parent.getWidth()-2*(int) context.getResources().getDimension(R.dimen.dip10),height/3);
        view.setLayoutParams(parms);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Challenge item = mDataset.get(position);

        if(item.getAlready_played() == 1)
        {
            holder.alreadyPlayedHldr.setVisibility(View.VISIBLE);
            holder.itemImg.setEnabled(false);
        }else {
            holder.alreadyPlayedHldr.setVisibility(View.GONE);
            holder.itemImg.setEnabled(true);
        }

        if (item.getCoverImage() != null && item.getCoverImage().contains("http"))
            Picasso.with(context).load(item.getCoverImage()).placeholder(R.drawable.dummy_user_icon).into(holder.itemImg);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(Challenge position, View v);
    }
}