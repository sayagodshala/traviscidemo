package com.merabreak;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AsyncLoader extends Dialog {
    public AsyncLoader(Context context) {
        super(context);
    }

    public AsyncLoader(Context context, int theme) {
        super(context, theme);
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static AsyncLoader show(Context context) {
        return show(context, "", true, null);
    }

    public static AsyncLoader show(Context context, CharSequence message,
                                   boolean cancelable, OnCancelListener cancelListener) {
        AsyncLoader dialog = dialog(context, message, cancelable, cancelListener);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        return dialog;
    }

    public static AsyncLoader dialog(final Context context) {
        return dialog(context, "", true, null);
    }

    public static AsyncLoader dialog(final Context context, final CharSequence message,
                                     final boolean cancelable,
                                     final OnCancelListener cancelListener) {
        AsyncLoader dialog = new AsyncLoader(context, R.style.FragmentDialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.async_loader);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCancelable(false);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
}
