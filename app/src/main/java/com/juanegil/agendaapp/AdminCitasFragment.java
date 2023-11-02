package com.juanegil.agendaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.juanegil.agendaapp.adapter.CitasAdapter;
import com.juanegil.agendaapp.database.AppDatabase;
import com.juanegil.agendaapp.databinding.FragmentAdminCitasBinding;
import com.juanegil.agendaapp.models.Cita;
import com.juanegil.agendaapp.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class AdminCitasFragment extends Fragment implements CitasAdapter.OnItemClicked {
    private FragmentAdminCitasBinding binding;
    List<Cita> listaCitas = new ArrayList<>();
    CitasAdapter citasAdapter = new CitasAdapter(listaCitas, this);
    AppDatabase db;
    Cita cita = new Cita();
    Boolean isValid = false;
    Boolean isEdit = false;

    public AdminCitasFragment() {
        // Required empty public constructor
    }

    public static AdminCitasFragment newInstance(String param1, String param2) {
        AdminCitasFragment fragment = new AdminCitasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminCitasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Administra tus Citas");

        db = new Utils().getAppDatabase(getContext());

        setupToolbarMenu();
        obtenerCitas();

        binding.svCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarCliente(newText);
                return false;
            }
        });

        return view;
    }

    private void filtrarCliente(String text) {
        ArrayList<Cita> listaFiltrada = new ArrayList<>();
        for (Cita cita : listaCitas) {
            if (cita.nombreCliente.toLowerCase().contains(text.toLowerCase())) {
                listaFiltrada.add(cita);
            }
        }
        citasAdapter.filtrarCliente(listaFiltrada);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvCitas.setLayoutManager(layoutManager);
        citasAdapter = new CitasAdapter(listaCitas, this);
        binding.rvCitas.setAdapter(citasAdapter);
    }

    private void setupToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_agregar) {
                    lanzarAlertDialogCita(getActivity());
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void lanzarAlertDialogCita(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View vista = inflater.inflate(R.layout.alert_dialog_add_update_citas, null);
        builder.setView(vista);
        builder.setCancelable(false);

        EditText etNombreCliente, etTelefonoCliente, etAsuntoCita;
        TextView tvTituloAlert, tvHora;
        ImageButton ibtnHora;
        Spinner spiDias;

        etNombreCliente = vista.findViewById(R.id.etNombreCliente);
        etTelefonoCliente = vista.findViewById(R.id.etTelefonoCliente);
        etAsuntoCita = vista.findViewById(R.id.etAsuntoCita);
        tvTituloAlert = vista.findViewById(R.id.tvTituloAlert);
        tvHora = vista.findViewById(R.id.tvHora);
        ibtnHora = vista.findViewById(R.id.ibtnHora);
        spiDias = vista.findViewById(R.id.spiDias);

        String[] listaDias = activity.getResources().getStringArray(R.array.dias_semana);
        ArrayAdapter arrayAdapter = new ArrayAdapter(activity, R.layout.item_spinner, listaDias);
        spiDias.setAdapter(arrayAdapter);

        if (isEdit) {
            tvTituloAlert.setText("ACTUALIZAR CITA");
            etNombreCliente.setText(cita.nombreCliente);
            etTelefonoCliente.setText(cita.telefonoCliente);
            etAsuntoCita.setText(cita.asuntoCliente);
            tvHora.setText(cita.horaCita);
            spiDias.setSelection(arrayAdapter.getPosition(cita.diaCita));
        }

        ibtnHora.setOnClickListener(view -> {
            obtenerHora(tvHora);
        });

        builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
            if (!isEdit) {
                cita.idCita = String.valueOf(System.currentTimeMillis());
            }
            cita.nombreCliente = etNombreCliente.getText().toString().trim();
            cita.telefonoCliente = etTelefonoCliente.getText().toString().trim();
            cita.asuntoCliente = etAsuntoCita.getText().toString().trim();
            cita.horaCita = tvHora.getText().toString().trim();
            cita.diaCita = spiDias.getSelectedItem().toString();

            validarCampos();
            if (isValid) {
                if (isEdit) {
                    actualizarCita();
                    isEdit = false;
                } else {
                    agregarCita();
                }
            } else {
                Toasty.error(getContext(), "Faltaron por llenar campos obligatorios", Toast.LENGTH_SHORT, true).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
        });

        builder.create();
        builder.show();
    }


    private void validarCampos() {
        if (cita.nombreCliente.isEmpty() || cita.telefonoCliente.isEmpty() || cita.horaCita.isEmpty() || cita.diaCita.contains("*")) {
            isValid = false;
        } else {
            isValid = true;
        }
    }

    private void obtenerHora(TextView tvHora) {
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String horaFormateada = (hourOfDay < 10) ? "0" + hourOfDay : String.valueOf(hourOfDay);
            String minutoFormateada = (minute < 10) ? "0" + minute : String.valueOf(minute);
            tvHora.setText(horaFormateada + ":" + minutoFormateada);
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
        recogerHora.show();
    }

    private void obtenerCitas() {
        AsyncTask.execute(() -> {
            listaCitas = db.citaDao().obtenerCitas();
            getActivity().runOnUiThread(() -> setupRecyclerView());
        });
    }

    private void agregarCita() {
        AsyncTask.execute(() -> {
            db.citaDao().agregarCita(cita);
            listaCitas = db.citaDao().obtenerCitas();
            getActivity().runOnUiThread(() -> setupRecyclerView());
        });
    }

    private void actualizarCita() {
        AsyncTask.execute(() -> {
            db.citaDao().actualizarCita(cita);
            listaCitas = db.citaDao().obtenerCitas();
            getActivity().runOnUiThread(() -> setupRecyclerView());
        });
    }

    @Override
    public void editarCita(Cita cita) {
        isEdit = true;
        this.cita = cita;
        lanzarAlertDialogCita(getActivity());
    }

    @Override
    public void borrarCita(Cita cita) {
        AsyncTask.execute(() -> {
            db.citaDao().elimniarCita(cita);
            listaCitas = db.citaDao().obtenerCitas();
            getActivity().runOnUiThread(() -> setupRecyclerView());
        });
        Toasty.info(getContext(), "Se elimimno el registro exitosamente", Toast.LENGTH_SHORT).show();
    }
}