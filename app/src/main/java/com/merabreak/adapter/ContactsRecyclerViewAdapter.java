package com.merabreak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.merabreak.FontUtils;
import com.merabreak.R;
import com.merabreak.models.ContactDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 11/16/2016.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView
        .Adapter<ContactsRecyclerViewAdapter
        .DataObjectHolder>/* implements Filterable*/ {

    private static MyClickListener myClickListener;
    List<String> contactName = new ArrayList<String>();
    List<String> contactName1 = new ArrayList<String>();
    List<String> mobileNum = new ArrayList<String>();
    public ArrayList<Integer> selectedIds = new ArrayList<Integer>();
    private int itemSelected = -1;
    public List<String> mobNum = new ArrayList<String>();
    public List<ContactDetails> contactDetails = new ArrayList<ContactDetails>();
    public List<ContactDetails> contactDetails1 = new ArrayList<ContactDetails>();
    public boolean onCheckedChanged = true;
    private SparseBooleanArray selectedContacts;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView contactName;
        TextView contactNum;
        Button contactSelect;

        public DataObjectHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactNum = (TextView) itemView.findViewById(R.id.contact_num);
            contactSelect = (Button) itemView.findViewById(R.id.contact_select);
            contactSelect.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.contact_select:
                    if (myClickListener != null) {
                        myClickListener.onCheckClick(getPosition(), v);
                    }
                    break;
                default:
                    break;
            }

        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ContactsRecyclerViewAdapter(List<ContactDetails> allContacts, Context context) {
        this.context = context;
        this.contactDetails = allContacts;
        this.contactDetails1 = new ArrayList<ContactDetails>(allContacts);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.referal_contact_list_item, parent, false);
        context = parent.getContext();
        FontUtils.setDefaultFont(context, view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ContactsRecyclerViewAdapter.DataObjectHolder holder, int position) {
        ContactDetails myContacts = contactDetails.get(position);

        holder.contactName.setText(myContacts.getContactName());
        holder.contactNum.setText(myContacts.getContactNum());

        if (selectedIds.contains(position)) {
            holder.contactSelect.setSelected(true);
        } else {
            holder.contactSelect.setSelected(false);
        }

    }

    public void toggleSelected(Integer position) {
        if (selectedIds.contains(position)) {
            selectedIds.remove(position);
            String mobNum1 = contactDetails.get(position).getContactNum();
            mobNum.remove(mobNum1);
        } else {
            selectedIds.add(position);
            String mobNum1 = contactDetails.get(position).getContactNum();
            mobNum.add(mobNum1);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactDetails.size();
    }

    public interface MyClickListener {
        //  public void onItemClick(int position, View v);
        public void onCheckClick(int position, View v);
    }

    public void filter(String text) {
        contactDetails.clear();
        if (text.isEmpty()) {
            contactDetails.addAll(contactDetails1);
        } else {
            text = text.toLowerCase();
            for (ContactDetails contacts : contactDetails1) {
                if (contacts.contactName.toLowerCase().contains(text) || contacts.contactNum.toLowerCase().contains(text)) {
                    contactDetails.add(contacts);
                }
            }
        }
        notifyDataSetChanged();
    }
}
