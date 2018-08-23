package com.merabreak.activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.merabreak.ApplicationSingleton;
import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.GoogleAnalyticEvents;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.adapter.ContactsRecyclerViewAdapter;
import com.merabreak.models.APIResponse;
import com.merabreak.models.ContactDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Vinay on 11/16/2016.
 */
@EActivity(R.layout.referal_contact_list)
public class ReferalContactsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.contact_search)
    EditText contact_search;

    @ViewById(R.id.contact_items)
    RecyclerView recyclerListItems;

    private ContactsRecyclerViewAdapter recyclerListItemsAdapter;

    List<String> contactName = new ArrayList<String>();
    List<String> mobileNum = new ArrayList<String>();

    public List<ContactDetails> contactDetails = new ArrayList<ContactDetails>();
    public List<ContactDetails> contactDetails1 = new ArrayList<ContactDetails>();
    ContactDetails contacts;

    List<String> contactName1 = new ArrayList<String>();

    private List<String> selectedNum = new ArrayList<String>();
    String name, phoneNumber, filteredNum;

    @AfterViews
    void init() {
        title.setText(R.string.referal_contacts_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        getContacts();
    }

    private void getContacts() {
        String lastNum = "";
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (pCur.moveToNext()) {
            name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (phoneNumber != null)
                filteredNum = phoneNumber.replaceAll("[\\-\\ ]", "");
            ContactDetails contacts = new ContactDetails();
            if (!lastNum.contains(filteredNum)) {
                lastNum = filteredNum;
                contacts.setContactName(name);
                contacts.setContactNum(filteredNum);
                contactDetails.add(contacts);
                contactDetails1.add(contacts);
            }
        }
        pCur.close();

        drawContacts();
    }

    private void drawContacts() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerListItems.setLayoutManager(manager);
        recyclerListItems.setNestedScrollingEnabled(false);
        recyclerListItems.setHasFixedSize(false);

        recyclerListItemsAdapter = new ContactsRecyclerViewAdapter(contactDetails, this);
        recyclerListItems.setAdapter(recyclerListItemsAdapter);

        ((ContactsRecyclerViewAdapter) recyclerListItemsAdapter).setOnItemClickListener(new ContactsRecyclerViewAdapter.MyClickListener() {
            /*   @Override
               public void onItemClick(int position, View v) {
                   ((ContactsRecyclerViewAdapter) recyclerListItemsAdapter).selectedItem(position);
               }
   */
            @Override
            public void onCheckClick(int position, View v) {
                ((ContactsRecyclerViewAdapter) recyclerListItemsAdapter).toggleSelected(position);
            }
        });
    }

    @Click(R.id.select_send)
    void slectContact() {
        GoogleAnalyticEvents.event(this, gaTracker, "App Invite", "User Invited His Contacts", user.getFullName() + ": " + user.getPhone() + ": " + new Date().toString());
      /*  Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.PARAM_9, "User invited his contacts");
        ((ApplicationSingleton) ReferalContactsActivity.this.getApplication()).appsFlyerLib().trackEvent(ReferalContactsActivity.this, "App invite", eventValue);*/
        selectedNum = ((ContactsRecyclerViewAdapter) recyclerListItemsAdapter).mobNum;
        //Log.d("selected contacts", selectedNum.toString());
        if (selectedNum.size() > 0)
            sendContacts();
        else
            Util.makeToast(this, getResources().getString(R.string.referal_select_contact));

    }

    private void sendContacts() {
        hideLoader(progress);
        showLoader(progress);
        Call<APIResponse> callback = apiService.referalContact(Constants.MB_API_KEY, user.getAuthToken(), selectedNum.toString());
        callback.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response) {
                hideLoader(progress);
                if(response.body() != null && response.code() == 200) {
                    if (response.body().getMeta() != null) {
                        if (response.body().getMeta().getStatusCode() == 401) {
                            if (response.body().getMeta().isStatus()) {
                                user.setAuthToken(response.body().getMeta().getNewAuthToken());
                                Util.setUser(ReferalContactsActivity.this, user);
                                sendContacts();
                            } else {
                                oopsLogout(response.body().getMeta().getMessage());
                            }
                        } else {
                            if (response.body().getMeta().isStatus()) {
                          /*  for (int i = 0; i < selectedNum.size(); i++) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(selectedNum.get(i), null, "hii this is vinay", null, null );
                            }*/
                                Util.makeToastLong(ReferalContactsActivity.this, response.body().getMeta().getMessage());
                                finish();
                            } else {
                                Util.makeToast(ReferalContactsActivity.this, response.body().getMeta().getMessage());
                            }
                        }
                    } else {
                        Util.makeToast(ReferalContactsActivity.this, Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(ReferalContactsActivity.this, Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader(progress);
                if (t != null)
                    Log.d("JSONError", t.getMessage());
                Util.makeToast(ReferalContactsActivity.this, Constants.SOME_THING_WRONG);
            }
        });
    }

    @TextChange(R.id.contact_search)
    void searchContact(CharSequence cs) {
        String filteredString = cs.toString().toLowerCase();
        recyclerListItemsAdapter.filter(filteredString);
    }
}
