package com.miki.justincase_v1.fragments.Edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miki.justincase_v1.Presented;
import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.entity.Baggage;
import com.miki.justincase_v1.db.entity.Suitcase;
import com.miki.justincase_v1.fragments.BaseFragment;

public class Fragment_Edit_Baggage extends BaseFragment {
    EditText suitcaseNameTV, suitcaseColorTV, suitcaseWeightTV, suitcaseDimnsTV;
    Suitcase suitcase;
    Baggage baggage;

    Button bnt;
    Binding_Entity_focusEntity binding_baggage_focusBaggage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_suitcase, container, false);

        suitcaseNameTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseName);
        suitcaseColorTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseNameColor);
        suitcaseWeightTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseWeight);
        suitcaseDimnsTV = view.findViewById(R.id.activity_createSuitcase_input_suitcaseDims);

        Bundle bundle = getArguments();

        if (bundle != null) {
            baggage = (Baggage) bundle.getSerializable("ThisBaggage");

            suitcase = Presented.getSuitcaseAsociatedWithThisBaggage(baggage, view);

            suitcaseNameTV.setText(suitcase.getSuitcaseName());
            suitcaseColorTV.setText(suitcase.getSuitcaseColor());
            suitcaseWeightTV.setText(suitcase.getSuitcaseWeight());
            suitcaseDimnsTV.setText(suitcase.getSuitcaseDims());

            String nombreMaleta = suitcaseNameTV.getText().toString();
            String color = suitcaseColorTV.getText().toString();
            String weight = suitcaseWeightTV.getText().toString();
            String dimns = suitcaseDimnsTV.getText().toString();

            bnt = view.findViewById(R.id.fragment_createSuitcase_btn_add);
            bnt.setOnClickListener(v -> {
                Presented.updateBaggage(baggage, nombreMaleta, color, weight, dimns, view);

                closeKeyBoard();
                binding_baggage_focusBaggage.sendBaggage(baggage);
            });
        }
        return view;
    }
}


