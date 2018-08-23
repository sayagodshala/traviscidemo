package com.merabreak;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;
import com.merabreak.activities.SplashActivity_;
import com.merabreak.db.DbHelper;
import com.merabreak.models.User;
import com.merabreak.network.APIClient;
import com.merabreak.network.APIService;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.subjects.PublishSubject;

public class BaseFragment extends Fragment {
    PublishSubject<Void> detached = PublishSubject.create();
    private AsyncLoader asyncLoader;
    private boolean isInit = false;
    protected Context context;
    public User user;
    public APIService apiService;
    public Tracker gaTracker;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncLoader = AsyncLoader.dialog(getActivity());
        apiService = APIClient.getAPIService();
        gaTracker = ((ApplicationSingleton) getActivity().getApplication()).googleAnalyticsTracker();
        initUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onPostCreateView(container);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    protected void onPostCreateView(View view) {
        FontUtils.setDefaultFont(getActivity(), view);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detached.onNext(null);
    }

    protected <T> Observable<T> bindObservable(Observable<T> in) {
        return AppObservable.bindFragment(this, in).takeUntil(detached);
    }

    public void showLoader(LinearLayout progress) {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(LinearLayout progress) {
        progress.setVisibility(View.GONE);
    }

    public void showLoader() {
        try {
            asyncLoader.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader() {
        try {
            asyncLoader.hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void error(final Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(() -> genericError(throwable));
    }

    private void genericError(final Throwable throwable) {
        String errorMessage =
                isNetworkError(throwable) ? "Network error" : "Some error occurred. Try again.";
        if (getActivity() != null) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            hideLoader();
        }
    }

    private boolean isNetworkError(final Throwable throwable) {
//        if (throwable instanceof RetrofitError) {
//            return ((RetrofitError) throwable).getKind().equals(RetrofitError.Kind.NETWORK);
//        }
        return false;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void oopsLogout(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(context.getResources().getString(R.string.label_oops));
        builder.setCancelable(false);
        builder.setMessage(message).setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
            clearAllUserDataAndFinish();
        }).show();
    }

    private void initUser() {
        user = Util.getUser(getActivity());
       /* if (user != null) {
            AppsFlyerLib.getInstance().setCustomerUserId(user.getPhone());
            AppsFlyerLib.getInstance().setUserEmails(user.getEmail());
        }*/
    }

    private void clearAllUserDataAndFinish() {
        DbHelper.getInstance(getActivity()).clearTable();
        SharedPreferences settings = getActivity().getSharedPreferences(AppSettings.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        SplashActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUser();
        if (isInit)
            hideLoader();
        isInit = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLoader();
    }

    public void setRecyclerViewLayoutManager(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    public void setRecyclerViewLayoutManagerHorizontal(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
    }

}
