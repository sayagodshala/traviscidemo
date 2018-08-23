package com.merabreak;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merabreak.models.Coin;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Ewebcore on 05-Feb-16.
 */
@EViewGroup(R.layout.list_item_coin)
public class CoinItem extends RelativeLayout implements BaseListItemView<Coin> {

    public Coin coin;
    private Context context;

    @ViewById(R.id.event)
    TextView event;

    @ViewById(R.id.action)
    TextView action;

    @ViewById(R.id.coins)
    TextView coins;

    public CoinItem(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bind(Coin item) {
        coin = item;
        event.setText((item.getEvent() != null && !item.getEvent().equalsIgnoreCase("")) ? item.getEvent() : "NA");
        action.setText(item.getAction());
        action.setText(item.getAction());
        coins.setText("Coins - " + item.getCoins() + ", Balance - " + item.getBalance());
    }
}