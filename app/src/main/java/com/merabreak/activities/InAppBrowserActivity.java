package com.merabreak.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.Constants;
import com.merabreak.MeraBreakWebViewClient;
import com.merabreak.R;
import com.merabreak.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.mcl_in_app_browser)
public class InAppBrowserActivity extends BaseActivity implements MeraBreakWebViewClient.MeraBreakWebViewClientListener {

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

    @Extra
    String url;

    @Extra
    String toolbarTitle;

    @AfterViews
    void init() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mcl_status_bar_color));
        }
        showLoader(progress);
        copyRight.setVisibility(View.GONE);

        if(!Util.textIsEmpty(toolbarTitle))
        {
            title.setText(toolbarTitle);
        }else {
            title.setText(R.string.ganesha_speaks_title);
        }
        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
        new MeraBreakWebViewClient(webView, url, this);
    }

    @Override
    public void onPageFinished() {
        hideLoader(progress);
    }
}
