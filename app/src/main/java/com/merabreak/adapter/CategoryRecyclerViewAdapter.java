package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.imageloader.ImageLoader;
import com.merabreak.models.challenge.Category;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView
        .Adapter<CategoryRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    private List<Category> mDataset;
    private static MyClickListener myClickListener;
    private Context context;
    private ImageLoader mImageLoader;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
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

    public CategoryRecyclerViewAdapter(List<Category> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Category item = mDataset.get(position);
        holder.title.setText(item.getTitle());

        if (item.getSlug().equalsIgnoreCase("animal")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.animal));
        } else if (item.getSlug().equalsIgnoreCase("book_literature")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.book_literature));
        } else if (item.getSlug().equalsIgnoreCase("college")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.book_literature));
        } else if (item.getSlug().equalsIgnoreCase("computer")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.computer));
        } else if (item.getSlug().equalsIgnoreCase("food_drink")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.food_drink));
        } else if (item.getSlug().equalsIgnoreCase("government_politics")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.government_politics));
        } else if (item.getSlug().equalsIgnoreCase("history")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.history));
        } else if (item.getSlug().equalsIgnoreCase("humor")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.humor));
        } else if (item.getSlug().equalsIgnoreCase("lifestyle_fashion")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.lifestyle_fashion));
        } else if (item.getSlug().equalsIgnoreCase("mind_body")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.mind_body));
        } else if (item.getSlug().equalsIgnoreCase("mobile")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile));
        } else if (item.getSlug().equalsIgnoreCase("money_business")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.money_business));
        } else if (item.getSlug().equalsIgnoreCase("people")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.people));
        } else if (item.getSlug().equalsIgnoreCase("pop_culture")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.pop_culture));
        } else if (item.getSlug().equalsIgnoreCase("puzzles_brainteasers")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.puzzles_brainteasers));
        } else if (item.getSlug().equalsIgnoreCase("random_knowledge")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.random_knowledge));
        } else if (item.getSlug().equalsIgnoreCase("science_nature")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.science_nature));
        } else if (item.getSlug().equalsIgnoreCase("scifi_fantasy")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.scifi_fantasy));
        } else if (item.getSlug().equalsIgnoreCase("sports")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.sport));
        } else if (item.getSlug().equalsIgnoreCase("tech_geography")) {
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.tech_geography));
        }

    }

    public void addItem(Category dataObj, int index) {
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