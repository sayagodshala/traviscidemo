package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.merabreak.R;
import com.merabreak.models.ShippingAddress;

import java.util.List;

public class ShippingAddressRecyclerViewAdapter extends RecyclerView
        .Adapter<ShippingAddressRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    private static String type = "";
    private List<ShippingAddress> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private int itemSelected = -1;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView fullName;
        TextView address;
        Button check, edit;

        public DataObjectHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.full_name);
            address = (TextView) itemView.findViewById(R.id.address);
            check = (Button) itemView.findViewById(R.id.check);
            edit = (Button) itemView.findViewById(R.id.edit);

            if (type.equalsIgnoreCase("selection")) {
                check.setVisibility(View.VISIBLE);
            }

            check.setOnClickListener(this);
            edit.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.check:
                    if (myClickListener != null)
                        myClickListener.onCheckClick(getPosition(), v);

                    break;
                case R.id.edit:

                    if (myClickListener != null)
                        myClickListener.onEditClick(getPosition(), v);

                    break;
                default:
//                    if (myClickListener != null)
//                        myClickListener.onItemClick(getPosition(), v);
                    break;
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ShippingAddressRecyclerViewAdapter(List<ShippingAddress> myDataset, Context context, String type) {
        this.context = context;
        this.mDataset = myDataset;
        this.type = type;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shipping_address, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ShippingAddress item = mDataset.get(position);
        holder.fullName.setText(item.getFullName());
        holder.address.setText((item.getAddressLine1() != null) ? item.getAddressLine1() : "");
        if (item.getAddressLine2() != null) {
            holder.address.setText(holder.address.getText().toString() + ", " + item.getAddressLine2());
        }

        if (item.isSelected())
            holder.check.setSelected(true);
        else
            holder.check.setSelected(false);

    }

    public void addItem(ShippingAddress dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void selectedItem(int index) {

        if (itemSelected < 0) {
            itemSelected = index;
            mDataset.get(itemSelected).setSelected(true);
        } else {
            mDataset.get(itemSelected).setSelected(false);
            mDataset.get(index).setSelected(true);
            itemSelected = index;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

        public void onEditClick(int position, View v);

        public void onCheckClick(int position, View v);
    }
}