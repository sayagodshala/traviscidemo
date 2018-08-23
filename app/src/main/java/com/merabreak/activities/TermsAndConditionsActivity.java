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

/**
 * Created by Vinay on 11/18/2016.
 */
@EActivity(R.layout.terms)
public class TermsAndConditionsActivity extends BaseActivity implements MeraBreakWebViewClient.MeraBreakWebViewClientListener {

    @ViewById(R.id.termsView)
    WebView webView;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void init() {
        showLoader(progress);
        title.setText(R.string.terms_title);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        new MeraBreakWebViewClient(webView, Constants.TERMS_PRIVACY_BASE_URL + "/termsandconditions.html", this);
    }

    @Override
    public void onPageFinished() {
        hideLoader(progress);
    }
}
