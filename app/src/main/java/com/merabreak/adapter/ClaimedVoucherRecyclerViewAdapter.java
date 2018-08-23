package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.ClaimedVoucherModel;
import com.merabreak.models.ClaimedVoucherModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ewebcoremac1 on 11/07/18.
 */

public class ClaimedVoucherRecyclerViewAdapter extends RecyclerView.Adapter<ClaimedVoucherRecyclerViewAdapter.DataObjectHolder> {

    private static String TAG = NotificationRecyclerViewAdapter.class.getName();
    private Context context;
    private ArrayList<ClaimedVoucherModel> mDataset;



    public ClaimedVoucherRecyclerViewAdapter(Context context, ArrayList<ClaimedVoucherModel> list) {
        this.context = context;
        this.mDataset = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_claimed_voucher, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        ClaimedVoucherRecyclerViewAdapter.DataObjectHolder dataObjectHolder = new ClaimedVoucherRecyclerViewAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ClaimedVoucherModel item = mDataset.get(position);


        if (item.getThumb_image() != null && item.getThumb_image().contains("http"))
            Picasso.with(context).load(item.getThumb_image()).placeholder(R.color.light_grey1).into(holder.thumbnailImg);


        try {

            if (item.getVoucher_code() != null && !item.getVoucher_code().isEmpty()) {
                holder.voucherNameTxt.setVisibility(View.VISIBLE);
                holder.voucherNameTxt.setText(item.getVoucher_code());
            } else
                holder.voucherNameTxt.setVisibility(View.GONE);

        }catch(NullPointerException e)
        {
            holder.voucherNameTxt.setVisibility(View.GONE);
            Log.e(TAG,e.toString());
        }
        catch(Exception e)
        {
            holder.voucherNameTxt.setVisibility(View.GONE);
            Log.e(TAG,e.toString());
        }

        try{
            if(item.getClaim_title() != null && !item.getClaim_title().isEmpty()) {
                holder.voucherApplicableTxt.setVisibility(View.VISIBLE);
                holder.voucherApplicableTxt.setText(item.getClaim_title());
            }else {
                holder.voucherApplicableTxt.setVisibility(View.GONE);
            }

        }catch(NullPointerException e)
        {
            holder.voucherApplicableTxt.setVisibility(View.GONE);
            Log.e(TAG,e.toString());
        }
        catch(Exception e)
        {
            holder.voucherApplicableTxt.setVisibility(View.GONE);
            Log.e(TAG,e.toString());
        }

        if(item.getClaim_description() !=null && !item.getClaim_description().isEmpty())
        {
            holder.contentTxt.setVisibility(View.VISIBLE);
            holder.contentTxt.setText(item.getClaim_description());
        }
        else
            holder.contentTxt.setVisibility(View.GONE);

        holder.arrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.claimHolder.getVisibility() == View.GONE)
                {
                    holder.claimHolder.setVisibility(View.VISIBLE);
                    holder.arrowImg.setImageResource(R.drawable.arrow_up);
                }
                else
                {
                    holder.claimHolder.setVisibility(View.GONE);
                    holder.arrowImg.setImageResource(R.drawable.arrow_down);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView voucherNameTxt , voucherApplicableTxt, contentTxt;
        ImageView arrowImg;
        LinearLayout claimHolder;
        private ImageView thumbnailImg;
        private RelativeLayout voucherImgHldr;

        public DataObjectHolder(View itemView) {
            super(itemView);
            voucherNameTxt = (TextView) itemView.findViewById(R.id.voucher_name_txt);
            voucherApplicableTxt = (TextView) itemView.findViewById(R.id.applicable_for_txt);
            arrowImg = (ImageView) itemView.findViewById(R.id.arrow_img);
            thumbnailImg = (ImageView) itemView.findViewById(R.id.voucher_img);
            claimHolder = (LinearLayout) itemView.findViewById(R.id.claim_hldr);
            voucherImgHldr = (RelativeLayout) itemView.findViewById(R.id.voucher_item_hldr);
            
            
            contentTxt = (TextView) itemView.findViewById(R.id.content_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public void setOnItemClickListener(ClaimedVoucherRecyclerViewAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(ClaimedVoucherModel position, View v);

        public void onItemClick(View v, String tag, int position);
    }

    private static MyClickListener myClickListener;

    public static ClaimedVoucherRecyclerViewAdapter.MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(ClaimedVoucherRecyclerViewAdapter.MyClickListener myClickListener) {
        ClaimedVoucherRecyclerViewAdapter.myClickListener = myClickListener;
    }
}
