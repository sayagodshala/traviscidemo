package com.merabreak.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
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

public class RaffleRecyclerViewAdapter extends RecyclerView
        .Adapter<RaffleRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "RaffleRecyclerViewAdapter";
    private List<Raffle> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;
    private int itemSelected = -1;
    long raffleCountDown;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView type, title, maxCoins, totalCoins, question, timeLeft, counts, increment, decrement, price , desc, points, time;
        ImageView image, lock;
        LinearLayout detail, additions;
        RelativeLayout selector;
        Button buy;
        LinearLayout frmMask;
        private int counter = 0;

        public DataObjectHolder(View itemView) {
            super(itemView);
           /* type = (TextView) itemView.findViewById(R.id.type);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
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
            buy.setOnClickListener(this);
            increment.setOnClickListener(this);
            decrement.setOnClickListener(this);
            frmMask.setOnClickListener(this);*/
            //new
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            image = (ImageView) itemView.findViewById(R.id.image);
            points = (TextView) itemView.findViewById(R.id.points);
            time = (TextView) itemView.findViewById(R.id.raffle_time);
            lock = (ImageView) itemView.findViewById(R.id.lock);
            //new
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (myClickListener != null)
                myClickListener.onItemClick(getPosition(), v);

            /*switch (v.getId()) {
                case R.id.frm_mask:

                    if (myClickListener != null)
                        myClickListener.setSelected(getPosition(), v);

                    break;
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
            }*/
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

    public RaffleRecyclerViewAdapter(List<Raffle> myDataset, Context context) {
        mImageLoader = new ImageLoader(context);
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        /*View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item_raffle, parent, false);*/
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raffle_store_recycler_list_new, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Raffle item = mDataset.get(position);
        /*holder.type.setText(item.getBrandName());
        holder.title.setText(item.getTitle());
        holder.price.setText("Rs." + item.getPrice() + "/-");
        holder.detail.setVisibility(View.GONE);
        holder.maxCoins.setText(item.getCoinsPerTickets());
        holder.totalCoins.setTag(item.getCoinsPerTickets());
        holder.question.setText("HOW MANY TICKETS? (" + item.getMaxTickets() + " tickets left)");

        String imag = item.getImage();
        Log.d("raffle image", imag);

        if (item.isSelected())
            holder.detail.setVisibility(View.VISIBLE);
        else
            holder.detail.setVisibility(View.GONE);

        Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_raffle_image).into(holder.image, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
            }
        });

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
        }*/

        //new
       // holder.time.setVisibility(View.VISIBLE);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date raffleDate = dateFormatter.parse(item.getEndDateTime());
            Calendar a = Calendar.getInstance();
            a.setTime(raffleDate);
            Calendar b = Calendar.getInstance();
            raffleCountDown = daysBetween(b, a);
            new CountDownTimer(raffleCountDown, 1000) {
                public void onTick(long millisUntilFinished) {
                   // String hms = String.format("%02dd:%02dh:%02dm", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    String hms = String.format("%02dd:%02dh:%02dm:%02ds", TimeUnit.MILLISECONDS.toDays(millisUntilFinished), TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    holder.points.setText(hms);
                }

                public void onFinish() {
                    holder.points.setText(R.string.main_spin_wait_close);
                }
            }.start();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.lock.setVisibility(View.GONE);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());
        holder.title.setTypeface(font);
        holder.time.setTypeface(font);
        holder.points.setTypeface(font);
        Picasso.with(context).load(item.getImage()).placeholder(R.drawable.dummy_raffle_image).into(holder.image);
        /*if (Integer.parseInt(item.getCoinsPerTickets()) < 1000)
            holder.points.setText(item.getCoinsPerTickets() + " " + context.getResources().getString(R.string.points));
        else
            holder.points.setText(format(Long.valueOf(item.getCoinsPerTickets())) + " " + context.getResources().getString(R.string.points));*/
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

    public void addItem(Raffle dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void setSelected(int index) {

        if (itemSelected < 0) {
            itemSelected = index;
            mDataset.get(itemSelected).setSelected(true);
        } else {
            mDataset.get(itemSelected).setSelected(false);

            if (itemSelected == index) {
                mDataset.get(index).setSelected(false);
                itemSelected = -1;
            } else {
                mDataset.get(index).setSelected(true);
                itemSelected = index;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

        public void setSelected(int position, View v);
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMillis(Math.abs(end - start));
    }
}