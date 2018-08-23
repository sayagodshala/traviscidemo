package com.merabreak.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerFragmentListener {
        public void onDateSet(String date);
    }

    private DatePickerFragmentListener datePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date, d = null, m = null, y = null;
        d = "" + day;
        m = month + 1 + "";
        y = "" + year;
//        date = day + "/" + m + "/" + y;
//        date = year + "-" + m + "-" + d;

        if (d.length() == 1)
            d = "0" + d;

        if (m.length() == 1)
            m = "0" + m;

        date = d + "/" + m + "/" + year;

        //Log.d("Date Selected", date);

        notifyDatePickerListener(date);
//        Log.d("ClassName", getActivity().getClass().toString());
//        if (getActivity().getClass().toString().equalsIgnoreCase("creategameactivity")) {
//            ((CreateGameActivity) getActivity()).onDateSet(date);
//        }
    }

    public DatePickerFragmentListener getDatePickerListener() {
        return this.datePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(String date) {
        if (this.datePickerListener != null) {
            this.datePickerListener.onDateSet(date);
        }
    }
}