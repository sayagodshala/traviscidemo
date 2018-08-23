package com.merabreak.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
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
@EActivity(R.layout.privacy)
public class WinnersGalleryActivity extends BaseActivity implements MeraBreakWebViewClient.MeraBreakWebViewClientListener {

    @ViewById(R.id.policyView)
    WebView webView;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.copyRight)
    TextView copyRight;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.progress)
    LinearLayout progress;

    @AfterViews
    void init() {
        showLoader(progress);
        copyRight.setVisibility(View.GONE);
        title.setText("Winner's Gallery");
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        new MeraBreakWebViewClient(webView, Constants.TERMS_PRIVACY_BASE_URL + "/appwinners.html", this);
    }

    @Override
    public void onPageFinished() {
        hideLoader(progress);
    }
}
