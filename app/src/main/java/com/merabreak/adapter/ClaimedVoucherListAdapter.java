package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.ClaimedVoucherModel;

import java.util.ArrayList;

/**
 * Created by ewebcoremac1 on 07/07/18.
 */

public class ClaimedVoucherListAdapter extends RecyclerView.Adapter<ClaimedVoucherListAdapter.DataObjectHolder> {

    private static String TAG = ClaimedVoucherListAdapter.class.getName();
    private Context context;
    private ArrayList<ClaimedVoucherModel> mDataset;


    public ClaimedVoucherListAdapter(Context context, ArrayList<ClaimedVoucherModel> list) {
        this.context = context;
        this.mDataset = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_claim_voucher, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        ClaimedVoucherListAdapter.DataObjectHolder dataObjectHolder = new ClaimedVoucherListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ClaimedVoucherModel item = mDataset.get(position);

        if(item.getVoucher_code()!=null && !item.getVoucher_code().toString().trim().isEmpty())
            holder.codeTxt.setText(item.getVoucher_code());

        if(item.getClaim_title()!=null && !item.getClaim_title().toString().trim().isEmpty())
            holder.titleTxt.setText(item.getClaim_title());

        if(item.getClaim_description()!=null && !item.getClaim_description().toString().trim().isEmpty())
            holder.descTxt.setText(item.getClaim_description());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView codeTxt ,titleTxt, descTxt;


        public DataObjectHolder(View itemView) {
            super(itemView);
            titleTxt= (TextView) itemView.findViewById(R.id.title_txt) ;
            descTxt = (TextView) itemView.findViewById(R.id.desc_txt) ;
            codeTxt = (TextView) itemView.findViewById(R.id.code_txt) ;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) {
                myClickListener.onItemClick(mDataset.get(getPosition()), v);
            }
        }
    }

    public void setOnItemClickListener(ClaimedVoucherListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(ClaimedVoucherModel position, View v);
    }

    private static MyClickListener myClickListener;

    public static ClaimedVoucherListAdapter.MyClickListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(ClaimedVoucherListAdapter.MyClickListener myClickListener) {
        ClaimedVoucherListAdapter.myClickListener = myClickListener;
    }
}
