package com.miki.justincase_v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.AppDatabase;
import com.miki.justincase_v1.db.entity.Item;
import com.miki.justincase_v1.db.entity.SuitcaseContent;

import java.util.ArrayList;

public class Adaptador_suitcaseContent extends RecyclerView.Adapter<Adaptador_suitcaseContent.SuitcaseContentViewHolder> implements View.OnClickListener {

    AppDatabase db;

    private ArrayList<SuitcaseContent> dataset;
    private View.OnClickListener listener;

    public Adaptador_suitcaseContent(Context context, ArrayList<SuitcaseContent> contenidoDeEstaMaleta) {
        this.dataset =  contenidoDeEstaMaleta;
    }

    public void setListener(View.OnClickListener listener){this.listener = listener;}

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }

    }

    @NonNull
    @Override
    public Adaptador_suitcaseContent.SuitcaseContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element_suitcasecontent, parent, false);
        v.setOnClickListener(this);
        SuitcaseContentViewHolder vh = new SuitcaseContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador_suitcaseContent.SuitcaseContentViewHolder holder, int position) {

        db = AppDatabase.getInstance(holder.nombreContenido.getContext());
        int id = dataset.get(position).getI_id();
        Item i = db.itemDAO().getItem(id);
        holder.nombreContenido.setText(i.getItemName());

    }

    @Override
    public int getItemCount() { return dataset == null ? 0 : dataset.size(); }

    public class SuitcaseContentViewHolder extends RecyclerView.ViewHolder{
        public TextView nombreContenido;


        public SuitcaseContentViewHolder(@NonNull View v) {
            super(v);
            nombreContenido = v.findViewById(R.id.recyclerview_suitcaseContent_suitcaseContentName);

        }
    }


}
