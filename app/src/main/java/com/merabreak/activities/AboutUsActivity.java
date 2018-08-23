package com.merabreak.activities;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merabreak.BaseActivity;
import com.merabreak.BuildConfig;
import com.merabreak.Constants;
import com.merabreak.MeraBreakWebViewClient;
import com.merabreak.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Vinay on 11/9/2016.
 */
@EActivity(R.layout.about_page)
public class AboutUsActivity extends BaseActivity implements MeraBreakWebViewClient.MeraBreakWebViewClientListener {

    @ViewById(R.id.appVersion)
    TextView appVersion;

    @ViewById(R.id.aboutView)
    WebView webView;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.progress)
    LinearLayout progress;
    // some text
   /* @ViewById(R.id.about_fb)
    TextView about_fb;
*/
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void init() {
        showLoader(progress);
        title.setText(R.string.about_screen_name);
        toolbar.setNavigationIcon(R.drawable.mb_back_icon);
        toolbar.setNavigationOnClickListener(ignored -> onBackPressed());
       /* if (Build.VERSION.SDK_INT >= 24)
            about_fb.setText(Html.fromHtml(getResources().getString(R.string.about_fb), Html.FROM_HTML_MODE_LEGACY));
        else
            about_fb.setText(Html.fromHtml(getResources().getString(R.string.about_fb)));*/
      //  about_fb.setText(R.string.about_fb);
        new MeraBreakWebViewClient(webView, Constants.TERMS_PRIVACY_BASE_URL + "/aboutus.html", this);
        appVersion.setText(getResources().getString(R.string.about_app_ver) + BuildConfig.VERSION_NAME);
    }

    @Override
    public void onPageFinished() {
        hideLoader(progress);
    }
}
