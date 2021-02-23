package com.miki.justincase_v1.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
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

    public void closeKeyBoard() {
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param dateTextView The EditText to put the Date
     */
    protected void showDatePickerDialog(EditText dateTextView) {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day) + " / " + twoDigits(month + 1) + " / " + year;
                dateTextView.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /**
     * @param selectedDateTextView
     * @param dateTextView
     */
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


    public void makeToast(Context context, String text) {
        Toast toast =
                Toast.makeText(context,
                        text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public void printWarningToast(Context context, String text) {
        Toast toast =
                Toast.makeText(context,
                        text, Toast.LENGTH_SHORT);
        toast.show();

    }

    @NotNull
    public AlertDialog.Builder makeNewAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setNegativeButton(getString(R.string.text_cancel), ((dialog, which) -> dialog.dismiss()));
        builder.setTitle(title);
        builder.setCancelable(true);
        return builder;
    }
}
