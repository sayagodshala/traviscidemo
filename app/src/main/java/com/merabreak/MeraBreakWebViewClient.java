package com.merabreak;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Vinay on 11/18/2016.
 */
public class MeraBreakWebViewClient {

    public interface MeraBreakWebViewClientListener {
        public void onPageFinished();
    }

    public MeraBreakWebViewClient(WebView view, String url, MeraBreakWebViewClientListener listener) {
        view.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                listener.onPageFinished();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                //Log.d("WebResourceResponse", String.valueOf(errorResponse));
            }
        });
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
    }
}
