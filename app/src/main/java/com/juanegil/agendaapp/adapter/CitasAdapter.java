package com.juanegil.agendaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanegil.agendaapp.R;
import com.juanegil.agendaapp.models.Cita;

import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.viewHolder> {

    List<Cita> listaCitas;
    public OnItemClicked onClick;

    public CitasAdapter(List<Cita> listaCitas, OnItemClicked onClick) {
        this.listaCitas = listaCitas;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CitasAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_citas, parent, false);
        return new viewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasAdapter.viewHolder holder, int position) {
        Cita cita = listaCitas.get(position);

        holder.tvNombreCliente.setText(cita.nombreCliente.toUpperCase());
        holder.tvTelefonoCliente.setText(cita.telefonoCliente.toUpperCase());
        holder.tvAsuntoCita.setText(cita.asuntoCliente.toUpperCase());
        holder.tvHoraCita.setText(cita.horaCita.toUpperCase());
        holder.tvDiaCita.setText(cita.diaCita.toUpperCase());

        holder.ibtnEditar.setOnClickListener(view -> {
            onClick.editarCita(cita);
        });

        holder.ibtnBorrar.setOnClickListener(view -> {
            onClick.borrarCita(cita);
        });
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCliente, tvTelefonoCliente, tvAsuntoCita, tvHoraCita, tvDiaCita;
        ImageButton ibtnEditar, ibtnBorrar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            tvTelefonoCliente = itemView.findViewById(R.id.tvTelefonoCliente);
            tvAsuntoCita = itemView.findViewById(R.id.tvAsuntoCita);
            tvHoraCita = itemView.findViewById(R.id.tvHoraCita);
            tvDiaCita = itemView.findViewById(R.id.tvDiaCita);
            ibtnEditar = itemView.findViewById(R.id.ibtnEditar);
            ibtnBorrar = itemView.findViewById(R.id.ibtnBorrar);
        }
    }

    public interface OnItemClicked {
        void editarCita(Cita cita);

        void borrarCita(Cita cita);
    }
}
