package com.miki.justincase_v1.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.Trip.DatePickerFragment;

import org.jetbrains.annotations.NotNull;

public class BaseFragment extends Fragment {

    protected NavController getNav() {
        return Navigation.findNavController(getActivity(), R.id.fragment);
    }

    protected NavController getNav(View v) {
        return Navigation.findNavController(v);
    }

    public void closeKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focus = getActivity().getCurrentFocus();
        if (focus == null) {
            focus = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showDatePickerDialog(EditText dateTextView) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 because January is zero
            final String selectedDate = twoDigits(day) + " / " + twoDigits(month + 1) + " / " + year;
            dateTextView.setText(selectedDate);
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    protected void showDatePickerDialog(EditText selectedDateTextView, EditText dateTextView) {
        String selectedDate = selectedDateTextView.getText().toString();
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day) + " / " + twoDigits(month + 1) + " / " + year;
                dateTextView.setText(selectedDate);
            }
        }, selectedDate);

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    //notifications
    public void makeToast(Context context, String text) {
        Toast toast =
                Toast.makeText(context,
                        text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
