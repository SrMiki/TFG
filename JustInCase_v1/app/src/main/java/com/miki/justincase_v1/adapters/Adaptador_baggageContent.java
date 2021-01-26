package com.miki.justincase_v1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.bindings.Binding_Entity_focusEntity;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.BaggageContent;
import com.miki.justincase_v1.db.entity.Item;

import java.util.ArrayList;

public class Adaptador_baggageContent extends RecyclerView.Adapter<Adaptador_baggageContent.BaggageContentViewHolder> implements View.OnClickListener {

    AppDatabase db;

    Binding_Entity_focusEntity bindingBaggageFocusBaggage;
    Activity activity;

    private ArrayList<BaggageContent> dataset;
    private View.OnClickListener listener;

    public Adaptador_baggageContent(Context context, ArrayList<BaggageContent> contenidoDeEstaMaleta) {
        this.dataset = contenidoDeEstaMaleta;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }

    }

    @NonNull
    @Override
    public BaggageContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_baggagecontent, parent, false);
        v.setOnClickListener(this);
        BaggageContentViewHolder vh = new BaggageContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BaggageContentViewHolder holder, int position) {

        db = AppDatabase.getInstance(holder.baggageContentName.getContext());

        int id = dataset.get(position).getFKitemID();
        Item i = db.itemDAO().getItem(id);
        holder.baggageContentName.setText(i.getItemName());

        int baggageContentID = dataset.get(position).baggageContentID;
        BaggageContent baggageContent = (BaggageContent) db.baggageContentDAO().getBaggageContent(baggageContentID);
        holder.baggageContentCount.setText(baggageContent.getBaggageCount());


        holder.baggageContentButtonIncrease.setOnClickListener(v ->{
            baggageContent.increaseThisItem();
            db.baggageContentDAO().updateBaggageContent(baggageContent);

            this.activity = (Activity) holder.context;
            bindingBaggageFocusBaggage = (Binding_Entity_focusEntity) this.activity;
            bindingBaggageFocusBaggage.sendBaggage(db.baggageDAO().getThisBagagge(baggageContent.FKbaggageID));

        });

        holder.baggageContentButtonDecrease.setOnClickListener(v ->{
            baggageContent.decreaseThisItem();
            db.baggageContentDAO().updateBaggageContent(baggageContent);

            this.activity = (Activity) holder.context;
            bindingBaggageFocusBaggage = (Binding_Entity_focusEntity) this.activity;
            bindingBaggageFocusBaggage.sendBaggage(db.baggageDAO().getThisBagagge(baggageContent.FKbaggageID));

        });


    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    public class BaggageContentViewHolder extends RecyclerView.ViewHolder {
        public TextView baggageContentName, baggageContentCount;
        public Button baggageContentButtonIncrease, baggageContentButtonDecrease;
        Context context;


        public BaggageContentViewHolder(@NonNull View v) {
            super(v);

            context = itemView.getContext();
            baggageContentName = itemView.findViewById(R.id.recyclerview_baggageContent_itemName);
            baggageContentCount = itemView.findViewById(R.id.recyclerview_baggageContent_itemCount);
            baggageContentButtonIncrease = itemView.findViewById(R.id.recyclerview_baggageContent_btn_increase);
            baggageContentButtonDecrease = itemView.findViewById(R.id.recyclerview_baggageContent_btn_decrease);

        }
    }


}
