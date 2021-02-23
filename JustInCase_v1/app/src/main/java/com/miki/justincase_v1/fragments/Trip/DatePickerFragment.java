package com.miki.justincase_v1.fragments.Trip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    int initialYear = -1;
    int initialMonth = -1;
    int initialDay = -1;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener, String selectedDate) {
        DatePickerFragment fragment = new DatePickerFragment();
        String[] split = selectedDate.split("/");
        int day = Integer.parseInt(split[0].trim());
        int month = Integer.parseInt(split[1].trim());
        int year = Integer.parseInt(split[2].trim());
        fragment.setListener(listener);
        fragment.initialYear = year;
        //January start in Zero!
        fragment.initialMonth = month-1;
        fragment.initialDay = day;
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        // Initial selected value
        if (initialYear == -1) {
            initialYear = c.get(Calendar.YEAR);
        }
        if (initialMonth == -1) {
            initialMonth = c.get(Calendar.MONTH);
        }
        if (initialDay == -1) {
            initialDay = c.get(Calendar.DAY_OF_MONTH);
        }
        // Selected date (initial value)
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, initialYear, initialMonth, initialDay);

        // Min and max date
        // Min >> today

        c.set(Calendar.YEAR, initialYear);
        c.set(Calendar.MONTH, initialMonth);
        c.set(Calendar.DAY_OF_MONTH, initialDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        // limit to 10 year the travel planing
        c.set(Calendar.YEAR, initialYear + 10);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        return datePickerDialog;
    }

}