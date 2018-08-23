package com.merabreak;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class Fragments {
    public static void loadContentFragment(FragmentActivity fragmentActivity, int containerId,
                                           Fragment fragment) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getSimpleName())
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                .commitAllowingStateLoss();
    }

    public static void replaceContentFragment(FragmentActivity fragmentActivity, int containerId,
                                              Fragment fragment, String tag) {
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                .commitAllowingStateLoss();
    }
}
