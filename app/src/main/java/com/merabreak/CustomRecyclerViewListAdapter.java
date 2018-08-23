package com.merabreak;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rx.functions.Func0;

public class CustomRecyclerViewListAdapter<T, V extends View & BaseListItemView<T>> extends RecyclerView.Adapter {
    private List<T> items;
    private Func0<V> viewBuilder;

    public CustomRecyclerViewListAdapter(final List<T> items, Func0<V> viewBuilder) {
        this.items = items;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
