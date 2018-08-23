package com.merabreak.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreRecyclerViewAdapter extends RecyclerView
        .Adapter<StoreRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    public List<Store> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title, desc;
        TextView coins, price, points;
        ImageView image, lock;

        public DataObjectHolder(View itemView) {
            super(itemView);
            /*title = (TextView) itemView.findViewById(R.id.title);
            coins = (TextView) itemView.findViewById(R.id.coins);
            price = (TextView) itemView.findViewById(R.id.price);
            image = (ImageView) itemView.findViewById(R.id.image);
            lock = (ImageView) itemView.findViewById(R.id.lock);*/
            //new
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            image = (ImageView) itemView.findViewById(R.id.image);
            lock = (ImageView) itemView.findViewById(R.id.lock);
            points = (TextView) itemView.findViewById(R.id.points);
            //new
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public StoreRecyclerViewAdapter(List<Store> myDataset, Context context) {
        this.context = context;
        mImageLoader = new ImageLoader(context);
        this.mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        /*View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_store, parent, false);*/
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raffle_store_recycler_list_new, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Store item = mDataset.get(position);
        /*holder.title.setText(item.getTitle());
        holder.coins.setText(item.getCoins());
        holder.price.setText("Rs." + item.getPrice() + "/-");

        if (item.getAvailable() != null && item.getAvailable().equalsIgnoreCase("1"))
            holder.lock.setVisibility(View.GONE);

        Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_product_image).into(holder.image);*/
        //new
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());
        holder.title.setTypeface(font);
        holder.points.setTypeface(font);
        Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_product_image).into(holder.image);
        if (Integer.parseInt(item.getCoins()) < 1000)
            holder.points.setText(item.getCoins() + " " + context.getResources().getString(R.string.points));
        else
            holder.points.setText(format(Long.valueOf(item.getCoins())) + " " + context.getResources().getString(R.string.points));
        if (item.getAvailable() != null && item.getAvailable().equalsIgnoreCase("1"))
            holder.lock.setVisibility(View.GONE);
        //new
    }

    private final char[] SUFFIXES = {'K', 'M', 'G'};

    public String format(long number) {
        // Convert to a string
        final String string = String.valueOf(number);
        // The suffix we're using, 1-based
        final int magnitude = (string.length() - 1) / 3;
        // The number of digits we must show before the prefix
        final int digits = (string.length() - 1) % 3 + 1;
        // Build the string
        char[] value = new char[4];
        for (int i = 0; i < digits; i++) {
            value[i] = string.charAt(i);
        }
        int valueLength = digits;
        // Can and should we add a decimal point and an additional number?
        if (digits == 1 && string.charAt(1) != '0') {
            value[valueLength++] = '.';
            value[valueLength++] = string.charAt(1);
        }
        value[valueLength++] = SUFFIXES[magnitude - 1];
        return new String(value, 0, valueLength);
    }

    public void addItem(Store dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}