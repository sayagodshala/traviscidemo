package com.merabreak.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.models.NotificationModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ewebcoremac1 on 02/07/18.
 */

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.DataObjectHolder> {

    private static String TAG = NotificationRecyclerViewAdapter.class.getName();
    private Context context;
    private ArrayList<NotificationModel> mDataset;


    public NotificationRecyclerViewAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.mDataset = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_notification_item, parent, false);
            context = parent.getContext();
            FontUtils.setDefaultFont(context, view);

        NotificationRecyclerViewAdapter.DataObjectHolder dataObjectHolder = new NotificationRecyclerViewAdapter.DataObjectHolder(view);
            return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        NotificationModel item = mDataset.get(position);

        if (item.getIcon() != null && item.getIcon().contains("http"))
            Picasso.with(context).load(item.getIcon()).placeholder(R.drawable.dummy_challenge_image).into(holder.contestImg);

        if (Util.textIsEmpty(item.getTitle()))
        {
            holder.titleTxt.setText(context.getResources().getString(R.string.congratulations));
        }else
        {
            holder.titleTxt.setText(item.getTitle());
        }

        if (Util.textIsEmpty(item.getSub_title()))
        {
            holder.subTitleTxt.setText(context.getResources().getString(R.string.you_won_head));
        }else
        {
            holder.subTitleTxt.setText(item.getSub_title());
        }

        if (item.getAgo() != null && !item.getAgo().toString().trim().isEmpty())
            holder.timeTxt.setText(item.getAgo());

        if (item.getDescription() != null && !item.getDescription().toString().trim().isEmpty())
            holder.detailTxt.setText(item.getDescription());

        if(item.getNotification_status().equals("read"))
        {
            holder.mainHldr.setBackground(ContextCompat.getDrawable(context,R.color.read_bg));
        }
        else
        {
            holder.mainHldr.setBackground(ContextCompat.getDrawable(context,R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTxt, subTitleTxt , timeTxt , detailTxt;
        ImageView contestImg;
        LinearLayout mainHldr;

        public DataObjectHolder(View itemView) {
            super(itemView);
            titleTxt = (TextView) itemView.findViewById(R.id.congratulation_txt) ;
            subTitleTxt = (TextView) itemView.findViewById(R.id.you_won_txt) ;
            timeTxt = (TextView) itemView.findViewById(R.id.date_time_txt) ;
            detailTxt = (TextView) itemView.findViewById(R.id.congrats_detail_txt) ;
            contestImg = (ImageView) itemView.findViewById(R.id.contest_img);
            mainHldr =(LinearLayout) itemView.findViewById(R.id.main_item_hldr);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public void setOnItemClickListener(NotificationRecyclerViewAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(NotificationModel position, View v);
    }

    private static MyClickListener myClickListener;

    public static NotificationRecyclerViewAdapter.MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(NotificationRecyclerViewAdapter.MyClickListener myClickListener) {
        NotificationRecyclerViewAdapter.myClickListener = myClickListener;
    }
}
