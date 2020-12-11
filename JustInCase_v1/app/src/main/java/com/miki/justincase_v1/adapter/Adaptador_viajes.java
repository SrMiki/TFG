package com.miki.justincase_v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miki.justincase_v1.R;
import com.miki.justincase_v1.db.entity.Viaje;

import java.util.ArrayList;

public class Adaptador_viajes extends RecyclerView.Adapter<Adaptador_viajes.MyViewHolder> implements View.OnClickListener {

    private ArrayList<Viaje> dataset;


    //variable listener
    private View.OnClickListener listener;


    // Contrusctor
    public Adaptador_viajes(Context context,
                            ArrayList<Viaje> myDataset) {
        dataset = myDataset;
    }

    // Crea una nueva "vista"
    @Override
    public Adaptador_viajes.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viaje, parent, false);

        v.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // obtiene el elemento del dataset en esa posicion
        String nombre = dataset.get(position).getDestino();
        String descripcion = dataset.get(position).getFecha();

        holder.nombreMaleta.setText(nombre);
        holder.descricpcion.setText(descripcion);


    }

    //Tamaño de la lista, 0 por defecto
    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    /**
     * Permite seleccionar elementos del recycler view
     *
     * @param listener
     */
    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nombreMaleta, descricpcion;

        public MyViewHolder(@NonNull View v) {
            super(v);
            nombreMaleta = v.findViewById(R.id.destinoDetalle);
            descricpcion = v.findViewById(R.id.detallePerfil);
        }
    }
}

