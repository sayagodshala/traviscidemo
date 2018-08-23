package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.merabreak.Util;
import com.merabreak.models.ClaimModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ewebcoremac1 on 03/07/18.
 */

public class VoucherListRecyclerViewAdapter extends RecyclerView.Adapter<VoucherListRecyclerViewAdapter.DataObjectHolder> {

    private static String TAG = NotificationRecyclerViewAdapter.class.getName();
    private Context context;
    private ArrayList<ClaimModel> mDataset;



    public VoucherListRecyclerViewAdapter(Context context, ArrayList<ClaimModel> list) {
        this.context = context;
        this.mDataset = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_vouchers, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        VoucherListRecyclerViewAdapter.DataObjectHolder dataObjectHolder = new VoucherListRecyclerViewAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ClaimModel item = mDataset.get(position);


        if (item.getThumb_image() != null && item.getThumb_image().contains("http"))
            Picasso.with(context).load(item.getThumb_image()).placeholder(R.color.light_grey1).into(holder.thumbnailImg);


        String title = "";
        if(item.getPrice() != 0 && item.getPrice() >0)
        {
            title = "Rs. "+item.getPrice();
        }

        if(!title.isEmpty())
        {
            title = title + " "+ item.getClaim_title();
        }
        if(title != null && !title.isEmpty()) {
            holder.voucherNameTxt.setVisibility(View.VISIBLE);
            holder.voucherNameTxt.setText(title);
        }
        else
            holder.voucherNameTxt.setVisibility(View.GONE);

        if(item.getLocation() != null && !item.getLocation().isEmpty()) {
            holder.voucherApplicableTxt.setVisibility(View.VISIBLE);
            try{
                holder.voucherApplicableTxt.setText(Util.getHtmlFormatString(context.getResources().getString(R.string.applicable_for_txt)
                                                    + "<b>" + item.getLocation() + "</b>"
                                                    + context.getResources().getString(R.string.users)));
            }catch(NullPointerException e)
            {
                holder.voucherApplicableTxt.setVisibility(View.GONE);
            }


        }else {
            holder.voucherApplicableTxt.setVisibility(View.GONE);
        }

        if(item.getVoucher_left() > 0 ) {

            holder.voucherItemHldr.setEnabled(true);
            holder.arrowImg.setEnabled(true);
            holder.voucherItemHldr.setAlpha(1);

            holder.voucherLeftTxt.setVisibility(View.VISIBLE);
            holder.titleCrossHldr.setVisibility(View.GONE);
            holder.subTitleCrossHldr.setVisibility(View.GONE);

            holder.voucherLeftTxt.setVisibility(View.VISIBLE);
            holder.voucherLeftTxt.setText(context.getResources().getString(R.string.voucher_left)
                    + item.getVoucher_left());

        }else {
            holder.arrowImg.setEnabled(false);
            holder.voucherItemHldr.setEnabled(false);
            holder.voucherItemHldr.setAlpha(0.5f);

            holder.titleCrossHldr.setVisibility(View.VISIBLE);
            holder.subTitleCrossHldr.setVisibility(View.VISIBLE);

            holder.voucherLeftTxt.setVisibility(View.VISIBLE);
            holder.voucherLeftTxt.setText(context.getResources().getString(R.string.voucher_left)
                    + item.getVoucher_left());

            holder.claimHolder.setVisibility(View.GONE);
            holder.arrowImg.setVisibility(View.GONE);
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

        holder.claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyClickListener().onItemClick(v,"claimClick",position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView voucherNameTxt , voucherApplicableTxt , voucherLeftTxt, contentTxt;
        ImageView arrowImg;
        LinearLayout claimHolder;
        Button claimBtn;
        private ImageView thumbnailImg;
        private RelativeLayout voucherItemHldr, titleCrossHldr, subTitleCrossHldr;

        public DataObjectHolder(View itemView) {
            super(itemView);
            voucherNameTxt = (TextView) itemView.findViewById(R.id.voucher_name_txt);
            voucherApplicableTxt = (TextView) itemView.findViewById(R.id.applicable_for_txt);
            voucherLeftTxt = (TextView) itemView.findViewById(R.id.voucher_left_txt) ;
            arrowImg = (ImageView) itemView.findViewById(R.id.arrow_img);
            thumbnailImg = (ImageView) itemView.findViewById(R.id.voucher_img);
            claimHolder = (LinearLayout) itemView.findViewById(R.id.claim_hldr);
            claimBtn = (Button) itemView.findViewById(R.id.claim_btn);
            voucherItemHldr = (RelativeLayout) itemView.findViewById(R.id.voucher_item_hldr);
            titleCrossHldr = (RelativeLayout) itemView.findViewById(R.id.title_cross_hldr);
            subTitleCrossHldr = (RelativeLayout) itemView.findViewById(R.id.sub_title_cross_hldr);
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

    public void setOnItemClickListener(VoucherListRecyclerViewAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(ClaimModel position, View v);

        public void onItemClick(View v, String tag, int position);
    }

    private static MyClickListener myClickListener;

    public static VoucherListRecyclerViewAdapter.MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(VoucherListRecyclerViewAdapter.MyClickListener myClickListener) {
        VoucherListRecyclerViewAdapter.myClickListener = myClickListener;
    }
}
