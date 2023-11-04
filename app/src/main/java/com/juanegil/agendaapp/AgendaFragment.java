package com.juanegil.agendaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.juanegil.agendaapp.adapter.CitasDiaAdapter;
import com.juanegil.agendaapp.databinding.FragmentAgendaBinding;
import com.juanegil.agendaapp.models.Cita;

import java.util.ArrayList;
import java.util.List;


public class AgendaFragment extends Fragment {

    FragmentAgendaBinding binding;
    List<Cita> listaCitas = new ArrayList<>();
    CitasDiaAdapter citasDiaAdapter;

    public AgendaFragment() {
        // Required empty public constructor
    }


    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgendaBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        return vista;
    }

    private void setupRecyclerView(RecyclerView rvDia, List<Cita> listaDia) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(layoutManager.VERTICAL);
        rvDia.setLayoutManager(layoutManager);
        citasDiaAdapter = new CitasDiaAdapter(listaDia);
        rvDia.setAdapter(citasDiaAdapter);
    }
}