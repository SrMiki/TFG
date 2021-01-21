package com.miki.justincase_v1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowTrips;

public class BaseFragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public void doFragmentTransaction(Fragment fragment) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragment);
        fragmentTransaction.commit();
    }

    public void doFragmentTransactionWithBundle(Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_layout, fragment);
        fragmentTransaction.commit();
    }

    public void closeKeyBoard() {
        InputMethodManager inputManager =
                (InputMethodManager) getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }



}
