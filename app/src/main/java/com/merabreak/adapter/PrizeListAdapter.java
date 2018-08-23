package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.NotificationModel;
import com.merabreak.models.PrizeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ewebcoremac1 on 06/07/18.
 */

public class PrizeListAdapter extends RecyclerView.Adapter<PrizeListAdapter.DataObjectHolder> {

    private static String TAG = PrizeListAdapter.class.getName();
    private Context context;
    private List<PrizeModel> list;

    public PrizeListAdapter(Context context, List<PrizeModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_prize, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        PrizeListAdapter.DataObjectHolder dataObjectHolder = new PrizeListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        PrizeModel item = list.get(position);

        if (item.getThumb_image() != null && item.getThumb_image().contains("http"))
            Picasso.with(context).load(item.getThumb_image()).placeholder(R.drawable.dummy_challenge_image).into(holder.image);

        if(item.getTitle()!=null && !item.getTitle().toString().trim().isEmpty())
            holder.title.setText(item.getTitle());

        if(position == list.size()-1)
            holder.viewHldr.setVisibility(View.GONE);
        else
            holder.viewHldr.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            {
        TextView title;
        ImageView image;
        RelativeLayout viewHldr;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_txt);

            image = (ImageView) itemView.findViewById(R.id.item_img);

            viewHldr = (RelativeLayout) itemView.findViewById(R.id.vertical_view_hldr);

        }
    }

}
