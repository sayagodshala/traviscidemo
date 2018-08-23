package com.merabreak;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import rx.functions.Func0;

public class CustomListAdapter<T, V extends View & BaseListItemView<T>> extends BaseAdapter {
    private List<T> items;
    private Func0<V> viewBuilder;

    public CustomListAdapter(final List<T> items, Func0<V> viewBuilder) {
        this.items = items;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(final int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        V viewItem = convertView == null || !(convertView instanceof BaseListItemView) ?
                viewBuilder.call() : (V) convertView;
        viewItem.bind(getItem(position));
        return viewItem;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(final List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Func0<V> getViewBuilder() {
        return viewBuilder;
    }

    public void setViewBuilder(final Func0<V> viewBuilder) {
        this.viewBuilder = viewBuilder;
    }
}
