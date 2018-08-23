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
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.Raffle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoryRaffleRecyclerViewAdapter extends RecyclerView
        .Adapter<HistoryRaffleRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "RaffleRecyclerViewAdapter";
    private List<Raffle> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView type, title, maxCoins, totalCoins, raffle_plan_buy, question, timeLeft, counts, increment, decrement, price;
        ImageView image;
        LinearLayout detail, additions;
        RelativeLayout selector;
        Button buy;
        LinearLayout frmMask;
        private int counter = 0;

        public DataObjectHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            price = (TextView) itemView.findViewById(R.id.price);
            raffle_plan_buy = (TextView) itemView.findViewById(R.id.raffle_plan_buy);
            title = (TextView) itemView.findViewById(R.id.title);
            question = (TextView) itemView.findViewById(R.id.question);
            timeLeft = (TextView) itemView.findViewById(R.id.time_left);
            image = (ImageView) itemView.findViewById(R.id.image);
            detail = (LinearLayout) itemView.findViewById(R.id.detail);
            additions = (LinearLayout) itemView.findViewById(R.id.additions);
            selector = (RelativeLayout) itemView.findViewById(R.id.selector);
            maxCoins = (TextView) itemView.findViewById(R.id.max_coins);
            buy = (Button) itemView.findViewById(R.id.buy);
            totalCoins = (TextView) itemView.findViewById(R.id.total_coins);
            counts = (TextView) itemView.findViewById(R.id.counts);
            increment = (TextView) itemView.findViewById(R.id.increment);
            decrement = (TextView) itemView.findViewById(R.id.decrement);
            frmMask = (LinearLayout) itemView.findViewById(R.id.frm_mask);
            itemView.setOnClickListener(this);
            buy.setOnClickListener(this);
            increment.setOnClickListener(this);
            decrement.setOnClickListener(this);
            frmMask.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.frm_mask:
//                    if (detail.getVisibility() == View.VISIBLE)
//                        detail.setVisibility(View.GONE);
//                    else
//                        detail.setVisibility(View.VISIBLE);
//                    break;
                case R.id.buy:
                    if (myClickListener != null)
                        myClickListener.onItemClick(getPosition(), v);
                    break;
                case R.id.increment:
                    counter++;
                    int maxItems = Integer.parseInt(maxCoins.getText().toString());
                    if (counter > maxItems)
                        counter = maxItems;
                    counts.setText(counter + "");
                    calculations();
                    break;
                case R.id.decrement:
                    counter--;
                    if (counter < 0)
                        counter = 0;
                    counts.setText(counter + "");
                    calculations();
                    break;
            }
        }

        private void calculations() {
            if (totalCoins.getTag() != null) {
                int coinsPerTicket = Integer.parseInt(totalCoins.getTag().toString());
                totalCoins.setText(counter * coinsPerTicket + "");
            }
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public HistoryRaffleRecyclerViewAdapter(List<Raffle> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item_raffle, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Raffle item = mDataset.get(position);
        holder.type.setText(item.getBrandName());
        holder.title.setText(item.getTitle());
        holder.price.setText("Rs." + item.getPrice() + "/-");
        holder.raffle_plan_buy.setVisibility(View.GONE);
        holder.maxCoins.setText(item.getCoinsPerTickets());
        holder.totalCoins.setTag(item.getCoinsPerTickets());
        holder.question.setText("HOW MANY TICKETS? (" + item.getMaxTickets() + " tickets left)");
        if (item.getImage() != null && item.getImage().contains("http")) {
            Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_challenge_image).into(holder.image, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date raffleDate = dateFormatter.parse(item.getEndDateTime());
            Calendar a = Calendar.getInstance();
            a.setTime(raffleDate);
            Calendar b = Calendar.getInstance();
            long c = daysBetween(b, a);

            if (c < 10)
                holder.timeLeft.setText("0" + c + " " + context.getResources().getString(R.string.history_raffle_days));
            else
                holder.timeLeft.setText(c + " " + context.getResources().getString(R.string.history_raffle_days));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addItem(Raffle dataObj, int index) {
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

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}