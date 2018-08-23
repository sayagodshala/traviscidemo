package com.merabreak;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Vinay on 7/26/2016.
 */
public class BaseDailogFragment extends DialogFragment {

    private AsyncLoader asyncLoader;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FragmentDialog);
        asyncLoader = AsyncLoader.dialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onPostCreateView(container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void onPostCreateView(View view) {
        FontUtils.setDefaultFont(getActivity(), view);
    }

    public void showLoader() {
        asyncLoader.show();
    }

    public void hideLoader() {
        asyncLoader.hide();
    }

    public void showLoader(LinearLayout progress) {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(LinearLayout progress) {
        progress.setVisibility(View.GONE);
    }

    public void error(final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> genericError(throwable));
    }

    private void genericError(final Throwable throwable) {
        Logger.logError(throwable);
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Some error occurred. Try again.", Toast.LENGTH_LONG)
                    .show();
            asyncLoader.hide();
        }
    }
}
