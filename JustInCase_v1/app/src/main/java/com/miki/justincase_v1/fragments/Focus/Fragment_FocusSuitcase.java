package com.miki.justincase_v1.fragments.Focus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;
import com.miki.justincase_v1.fragments.Edit.Fragment_Edit_Suitcase;
import com.miki.justincase_v1.fragments.Show.Fragment_ShowSuitcases;

public class Fragment_FocusSuitcase extends BaseFragment {

    TextView suitcaseName, suitcaseColor, suticaseWeight, suitcaseDimns;
    Button btn_focusSuitcase_delete, btn_focusSuitcase_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_suitcase, container, false);

        suitcaseName = view.findViewById(R.id.fragment_focusSuitcase_suitcaseName);
        suitcaseColor = view.findViewById(R.id.fragment_focusSuitcase_suitcaseColor);
        suticaseWeight = view.findViewById(R.id.fragment_focusSuitcase_suitcaseWeight);
        suitcaseDimns = view.findViewById(R.id.fragment_focusSuitcase_suitcaseDimns);

        Bundle bundle = getArguments();
        Suitcase suitcase;
        if (bundle != null) {
            suitcase = (Suitcase) bundle.getSerializable("suitcase");

            suitcaseName.setText(suitcase.getSuitcaseName());
            suitcaseColor.setText(suitcase.getSuitcaseColor());
            suticaseWeight.setText(suitcase.getSuitcaseWeight());
            suitcaseDimns.setText(suitcase.getSuitcaseDims());

            btn_focusSuitcase_delete = view.findViewById(R.id.fragment_focusSuitcase_btn_delete);
            btn_focusSuitcase_delete.setOnClickListener(v -> {
                Presented.deleteSuitcase(suitcase, view);
                getNav().navigate(R.id.fragment_ShowSuitcases);
            });

            btn_focusSuitcase_edit = view.findViewById(R.id.fragment_focusSuitcase_btn_edit);
            btn_focusSuitcase_edit.setOnClickListener(v -> {
                Bundle obundle = new Bundle();
                obundle.putSerializable("ThisSuitcase", suitcase);
                getNav().navigate(R.id.fragment_Edit_Suitcase, obundle);
            });
        }
        return view;
    }
}
