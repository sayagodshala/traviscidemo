package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;

import com.merabreak.models.RedeemVoucherModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ewebcoremac1 on 07/07/18.
 */

public class RedeemVoucherListAdapter extends RecyclerView.Adapter<RedeemVoucherListAdapter.DataObjectHolder> {

    private static String TAG = RedeemVoucherListAdapter.class.getName();
    private List<RedeemVoucherModel> mDataset;
    private Context context;
    private ImageLoader mImageLoader;
    private static MyClickListener myClickListener;

    public static MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(MyClickListener myClickListener) {
        RedeemVoucherListAdapter.myClickListener = myClickListener;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView itemImg , itemBgImg;
        ImageView rightArrowImg;
        TextView titleTxt, descTxt;
        RelativeLayout mainHolder;

        public DataObjectHolder(View itemView) {
            super(itemView);

            mainHolder = (RelativeLayout) itemView.findViewById(R.id.main_item_hldr);
            itemImg = (RoundedImageView) itemView.findViewById(R.id.item_img);
            itemBgImg = (RoundedImageView) itemView.findViewById(R.id.item_bg_img);
            rightArrowImg = (ImageView) itemView.findViewById(R.id.right_arrow_img);
            titleTxt = (TextView) itemView.findViewById(R.id.item_head_tv);
            descTxt = (TextView) itemView.findViewById(R.id.item_desc_tv);

            mainHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public RedeemVoucherListAdapter(List<RedeemVoucherModel> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
        mImageLoader = new ImageLoader(context);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_redeem_voucher, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);

        /*int height = parent.getHeight() - 2*(int) context.getResources().getDimension(R.dimen.dip20);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(parent.getWidth()-2*(int) context.getResources().getDimension(R.dimen.dip10),height/3);
        view.setLayoutParams(parms);*/
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        RedeemVoucherModel item = mDataset.get(position);

        try {
            if (!item.getTitle().isEmpty()) {
                holder.titleTxt.setText(item.getTitle());
            }
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, e.toString());
        }catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }

        try {
            if (item.getRaffles() > 0) {
                holder.descTxt.setVisibility(View.VISIBLE);
                holder.descTxt.setText(item.getRaffles()+ context.getResources().getString(R.string.raffle_tickets));
            }
            else if (item.getProducts() > 0) {
                holder.descTxt.setVisibility(View.VISIBLE);
                holder.descTxt.setText(item.getProducts()+ context.getResources().getString(R.string.product_tickets));
            }
            else {
                holder.descTxt.setVisibility(View.GONE);
            }

        }
        catch (NullPointerException e)
        {
            holder.descTxt.setVisibility(View.GONE);
            Log.e(TAG, e.toString());
        }catch (Exception e)
        {
            holder.descTxt.setVisibility(View.GONE);
            Log.e(TAG, e.toString());
        }

        try {

        }
        catch (NullPointerException e)
        {
            Log.e(TAG, e.toString());
        }catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }

        if (item.getIcon_image() != null && item.getIcon_image().contains("http"))
            Picasso.with(context).load(item.getIcon_image()).placeholder(R.drawable.dummy_image_loading).into(holder.itemImg);

        if(item.getBg_image() != null && item.getBg_image().contains("http"))
            Picasso.with(context).load(item.getBg_image()).placeholder(R.drawable.dummy_image_loading).into(holder.itemBgImg);
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
        public void onItemClick(RedeemVoucherModel position, View v);
    }
}