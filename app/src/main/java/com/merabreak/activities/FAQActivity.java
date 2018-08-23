package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MeraBreakWebViewClient;
import com.merabreak.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.privacy)
public class FAQActivity extends BaseActivity implements MeraBreakWebViewClient.MeraBreakWebViewClientListener {

    @ViewById(R.id.policyView)
    WebView webView;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @AfterViews
    void init() {
        showLoader(progress);
        title.setText(R.string.faq_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        new MeraBreakWebViewClient(webView, Constants.TERMS_PRIVACY_BASE_URL + "/appfaq.html", this);
    }

    @Override
    public void onPageFinished() {
        hideLoader(progress);
    }
}

