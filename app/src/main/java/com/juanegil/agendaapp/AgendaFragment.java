package com.juanegil.agendaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.juanegil.agendaapp.databinding.FragmentAgendaBinding;


public class AgendaFragment extends Fragment {


    FragmentAgendaBinding binding;

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
}